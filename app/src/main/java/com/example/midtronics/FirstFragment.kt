package com.example.midtronics


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.midtronics.databinding.FragmentFirstBinding
import android.widget.Button
import android.widget.LinearLayout

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation

import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonContainer = view.findViewById<LinearLayout>(R.id.button_container)


        val countries = parseXmlUsingPullParser(requireContext(),"countries/Countries.xml")

        val searchBar = view.findViewById<AutoCompleteTextView>(R.id.search_bar)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, countries)
        searchBar.setAdapter(adapter)

        searchBar.setOnItemClickListener { parent, view, position, id ->
            val selectedCountry = parent.getItemAtPosition(position).toString()
            val navController = findNavController()  // This should work if within the Fragment


            GlobalScope.launch {
                val jsonDataString = makeGetRequest(selectedCountry)

                withContext(Dispatchers.Main) {
                    if (jsonDataString != null) {
                        // Navigate using the correct NavController
                        val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(jsonDataString,selectedCountry)
                        navController.navigate(action)  // Use the correct NavController
                    } else {
                        Log.d("data", "Data is null")
                    }
                }
            }
        }

        for (c in countries) {
            val button = Button(requireContext()) // Create a new Button
            button.text = c // Set the button's text
            button.id = View.generateViewId() // Generate a unique ID for the button

            // Set layout parameters (for LinearLayout)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            button.layoutParams = params
            button.gravity = android.view.Gravity.CENTER
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.medium_bg))
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.text))
            params.setMargins(16, 10, 16, 10)

            button.setOnClickListener {

                val countrySelected = (it as Button).text.toString()
                Log.d("ButtonClicked", "Button text: $countrySelected")
                //Start an asynchronous coroutine



                GlobalScope.launch {
                    val jsonDataString = makeGetRequest(countrySelected)

                    withContext(Dispatchers.Main) {
                        if (jsonDataString != null) {
                            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(jsonDataString,countrySelected)
                            Navigation.findNavController(view).navigate(action)
                        } else {
                            Log.d("data", "Data is null")
                        }
                    }
                }


            }



            // Add the button to the container
            buttonContainer.addView(button) // This adds the button to the layout
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun parseXmlUsingPullParser(context: Context, fileName: String): List<String> {
        val tag = "XMLParsing"

        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open(fileName)
        val countries = mutableListOf<String>()

        val parser = Xml.newPullParser()
        parser.setInput(inputStream, null)

        try {
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {

                    XmlPullParser.TEXT -> {
                        val text = parser.text.trim()
                        if (text.isNotBlank()) {
                            countries.add(text)
                        }
                    }
                }
                eventType = parser.next() // Move to the next event
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream.close() // Always close the stream
        }

        return countries
    }

    fun parseData(data:String){

    }

    suspend fun makeGetRequest(country: String): String? {
        val api = "https://restcountries.com/v3.1/name/$country"
        Log.d("da", api)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(api)
            .build()

        var response: Response? = null
        var responseBody: String? = null
        try {
            response = client.newCall(request).execute()
            return  response.body?.string() ?: ""


        } catch (e: Exception) {
            Log.e("F", "Error making GET request", e)
        } finally {
            response?.close()
        }
        return null
    }





}