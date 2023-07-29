package com.lonewolf.jymstar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentExerciseDetailsBinding
import com.lonewolf.jymstar.resources.ShortCut_To
import com.lonewolf.jymstar.resources.Storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExerciseDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExerciseDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentExerciseDetailsBinding
    private lateinit var storage: Storage

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
        val view = inflater.inflate(R.layout.fragment_exercise_details, container, false)
        binding = FragmentExerciseDetailsBinding.bind(view)
        storage = Storage(requireContext())

        getData()
        return view
    }

    private fun getData() {
        val selected = (activity as MainActivity).selectedExArr

        Glide.with(requireContext()).load(selected["Url"]).into(binding.imgPic)
        binding.txtName.text = selected["Name"]

        binding.txtEquipdetail.text = selected["Equipment"]
        binding.txtBodyDetail.text = selected["Body Part"]

        Glide.with(requireContext()).load(ShortCut_To.getEquipbyEx(selected["Equipment"]?:"None")).into(binding.imgEquip)
        Glide.with(requireContext()).load(ShortCut_To.getBodyPartDraw(selected["MainId"]?:"None")).into(binding.imgBody)

        if(!selected["Rep"].equals("null")){
            binding.txtRepsDets.text = selected["Rep"]
        }

        if(!selected["Count"].equals("null")){
            binding.txtSetDets.text = selected["Count"]
        }

        if(!selected["Time"].equals("null")){
            binding.txtTimeDets.text = selected["Time"]
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExerciseDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExerciseDetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}