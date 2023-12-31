package com.lonewolf.jymstar.fragments.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentBmiCalculatorBinding
import com.lonewolf.jymstar.resources.ShortCut_To

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BmiCalculator.newInstance] factory method to
 * create an instance of this fragment.
 */
class BmiCalculator : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentBmiCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bmi_calculator, container, false)
        binding = FragmentBmiCalculatorBinding.bind(view)
        getButtons()

        return view
    }

    private fun getButtons() {
           binding.btnSubmit.setOnClickListener {
               if(binding.edtMeters.text.toString().isEmpty()){
                   binding.edtMeters.error = "Enter you height in meters"
                   binding.edtMeters.requestFocus()
               }else if(binding.edtMeters.text.toString().isEmpty()){
                    binding.edtKg.error = " Enter you weight in kilograms"
                   binding.edtKg.requestFocus()
               }else {
                   ShortCut_To.hideKeyboard(requireActivity())
                   ShortCut_To.calculateBMI(binding.edtKg.text.toString().toDouble(), binding.edtMeters.text.toString().toDouble(),
                   binding.txtBmi, binding.txtBmiLabel, binding.txtExplain)
               }
           }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BmiCalculator().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}