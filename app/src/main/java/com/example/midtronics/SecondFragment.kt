package com.example.midtronics

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.midtronics.databinding.FragmentSecondBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.TextView
import android.util.Log
import android.view.Gravity
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import kotlin.reflect.full.memberProperties


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jsonDataString = arguments?.getString("jsonDataString")


        if(jsonDataString !=null) {
            val gson = Gson()
            val countryList: List<Country> =
                gson.fromJson(jsonDataString, object : TypeToken<List<Country>>() {}.type)



            val firstCountry = countryList.getOrNull(0) // Get the first country, null if list is empty
            var imageSearchName = ""
            firstCountry?.let { country ->
                for (property in Country::class.memberProperties) {
                    var propertyName = property.name
                    var propertyValue = property.get(country) // Get the value of the property


                    if (propertyName == "name" && propertyValue is Map<*, *>) {
                        val nameMap = propertyValue as? Map<*, *>
                        val commonName = nameMap?.get("common") as? String
                        propertyValue = commonName

                        val countryNameTextView = view.findViewById<TextView>(R.id.countryName)

                        countryNameTextView.text = propertyValue  // Set the desired text
                        if( commonName != null){
                            imageSearchName = commonName
                        }

                        continue

                    }
                    else if (propertyValue is List<*> ){
                        val listString = propertyValue.joinToString(", ")
                        propertyValue = listString


                    }

                    propertyName = propertyName.first().uppercaseChar() + propertyName.substring(1)

                    val linearLayout = view.findViewById<LinearLayout>(R.id.card_container)

                    if (linearLayout != null) {


                        val cardView = CardView(requireContext())
                        cardView.cardElevation = 8f  // Elevation for shadow effect
                        cardView.radius = 40f  // Rounded corners
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.medium_bg
                            )  // Background color
                        )
                        cardView.setPadding(16, 8, 16, 8)  // Padding for the card

                        // Create a horizontal LinearLayout inside the CardView
                        val innerLinearLayout = LinearLayout(requireContext())
                        innerLinearLayout.orientation = LinearLayout.HORIZONTAL
                        innerLinearLayout.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )


                        // Create a TextView on the left side
                        val leftText = TextView(requireContext())
                        leftText.text = propertyName
                        leftText.setTextAppearance(R.style.DataEntry)
                        leftText.setPadding(40, 20, 20, 20)
                        innerLinearLayout.addView(leftText)

                        // Create a TextView on the right side
                        val rightText = TextView(requireContext())
                        rightText.text = propertyValue.toString()
                        rightText.setTextAppearance(R.style.DataEntry)
                        rightText.setPadding(20, 20, 40, 20)
                        // Use weight to push the right text to the far end
                        val weightParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f  // Assign a weight to use available space
                        )
                        rightText.layoutParams = weightParams
                        rightText.gravity = Gravity.END  // Align text to the right

                        innerLinearLayout.addView(rightText)  // Add the right text entry

                        // Add the inner LinearLayout to the CardView
                        cardView.addView(innerLinearLayout)

                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.setMargins(0, 16, 0, 16)  // Margins around the card

                        cardView.layoutParams = layoutParams

                        linearLayout.addView(cardView)  // Add CardView to the parent LinearLayout

                    } else {
                        Log.e("SecondFragment", "LinearLayout with ID 'card_container' is null.")
                    }
                }
            }
            var name = arguments?.getString("name")

            if (name == null){
                name = "Error"
            }

            val executor = Executors.newSingleThreadExecutor()
            var image: Bitmap? = null

            executor.execute {
                var inputStream: InputStream? = null
                var imageUrl: String? = null
                try {
                    val lcSelected = name.first().lowercase() + name.substring(1)
                    imageUrl = "https://www.worldmapsonline.com/content/lightbox/$lcSelected.jpg"
                    try {
                        inputStream = URL(imageUrl).openStream() // Open the image stream
                    } catch (e: IOException) {
                        try {
                            imageUrl = "https://www.worldmapsonline.com/content/lightbox/$name.jpg"
                            inputStream = URL(imageUrl).openStream()
                        } catch (e: IOException) {
                            try {
                                val combinedName = name.split(" ").joinToString("")
                                imageUrl = "https://www.worldmapsonline.com/content/lightbox/$combinedName.jpg"
                                inputStream = URL(imageUrl).openStream()
                            } catch (e: IOException) {
                                try {
                                    val combinedName = imageSearchName.split(" ").joinToString("")
                                    imageUrl = "https://www.worldmapsonline.com/content/lightbox/$combinedName.jpg"
                                    inputStream = URL(imageUrl).openStream()
                                }catch (e:IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }

                    image = BitmapFactory.decodeStream(inputStream) // Decode the Bitmap

                    // Update the ImageView on the main thread
                    requireActivity().runOnUiThread {
                        binding.imageView.setImageBitmap(image)
                    }
                } catch (e: IOException) {
                    e.printStackTrace() // Handle IOException
                } finally {
                    // Always close the input stream
                    inputStream?.close()
                }
            }

        }else{
            Log.e("Error", "No Data")
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




