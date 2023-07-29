package com.lonewolf.jymstar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentBodyBinding
import com.lonewolf.jymstar.databinding.LayoutPartsBinding
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Body.newInstance] factory method to
 * create an instance of this fragment.
 */
class Body : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentBodyBinding
    private lateinit var databaseReference: DatabaseReference
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
        val view = inflater.inflate(R.layout.fragment_body, container, false)
        binding = FragmentBodyBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference

        getButtons()
        return view
    }

    private fun getButtons() {
        getParts("")

    }

    private fun getParts(s: String) {
        val layoutInflater = LayoutInflater.from(requireContext())
        for(a in 0 until ShortCut_To.getBodyParts().size){

            var view: View?
            if(a%2==0) {
                view = layoutInflater.inflate(R.layout.layout_parts, binding.linLeft, false)
            }else{
                view = layoutInflater.inflate(R.layout.layout_parts, binding.linRight, false)

            }
            val bind = LayoutPartsBinding.bind(view)
            bind.txtPart.text = ShortCut_To.getBodyParts()[a]
            Picasso.get().load(ShortCut_To.getBodyPartsDraw()[a]).into(bind.imgPic)

            if(a%2==0) {
                binding.linLeft.addView(view)
            }else {
                binding.linRight.addView(view)
            }

            bind.cardMain.setOnClickListener {
                (activity as MainActivity).selectedCat = "Muscle"
                (activity as MainActivity).selected = ShortCut_To.getBodyParts()[a]
                (activity as MainActivity).navTo(com.lonewolf.jymstar.fragments.equipment.List(), "Body", storage.fragValPrev!!, 1)
            }

        }

    }

    override fun onResume() {
        if((activity as MainActivity).binding.txtTopic.text == ""){
            (activity as MainActivity).binding.txtTopic.text = "Welcome"
        }else{
            (activity as MainActivity).binding.txtTopic.text = "Body"
        }

        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Body.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Body().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}