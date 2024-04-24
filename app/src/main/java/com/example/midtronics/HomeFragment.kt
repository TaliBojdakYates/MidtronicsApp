package com.example.midtronics


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.midtronics.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setImageResource(R.drawable.headshot)
        // Access the views through binding
        val cardTitle = binding.cardTitle
        val collapsibleContent = binding.collapsibleContent
        val cardTitle2 = binding.cardTitle2
        val collapsibleContent2 = binding.collapsibleContent2
        val cardTitle3 = binding.cardTitle3
        val collapsibleContent3= binding.collapsibleContent3
        val cardTitle4 = binding.cardTitle4
        val collapsibleContent4= binding.collapsibleContent4

        cardTitle.setOnClickListener {
            val isExpanded = collapsibleContent.visibility == View.VISIBLE
            collapsibleContent.visibility = if (isExpanded) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        cardTitle2.setOnClickListener {
            val isExpanded = collapsibleContent2.visibility == View.VISIBLE
            collapsibleContent2.visibility = if (isExpanded) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        cardTitle3.setOnClickListener {
            val isExpanded = collapsibleContent3.visibility == View.VISIBLE
            collapsibleContent3.visibility = if (isExpanded) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        cardTitle4.setOnClickListener {
            val isExpanded = collapsibleContent4.visibility == View.VISIBLE
            collapsibleContent4.visibility = if (isExpanded) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        val navigateButton: Button = view.findViewById(R.id.navigate_button)
        navigateButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_bg))
        navigateButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.text))
        navigateButton.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_FirstFragment)  // Navigate using action ID
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




