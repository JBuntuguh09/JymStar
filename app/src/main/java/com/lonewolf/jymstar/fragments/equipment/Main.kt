package com.lonewolf.jymstar.fragments.equipment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentMainBinding
import com.lonewolf.jymstar.databinding.LayoutEquipmentBinding
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Main.newInstance] factory method to
 * create an instance of this fragment.
 */
class Main : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMainBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

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
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        getButtons()
        return view
    }

    private fun getButtons() {
        getParts("")
    }

    private fun getParts(s: String) {
        val layoutInflater = LayoutInflater.from(requireContext())
        for (a in 0 until ShortCut_To.getEquip().size) {

            var view: View?
            if (a % 2 == 0) {
                view = layoutInflater.inflate(R.layout.layout_equipment, binding.linLeft, false)
            } else {
                view = layoutInflater.inflate(R.layout.layout_equipment, binding.linRight, false)

            }
            val bind = LayoutEquipmentBinding.bind(view)
            bind.txtPart.text = ShortCut_To.getEquip()[a]
            //Picasso.get().load(ShortCut_To.getBodyEquipmentDraw()[a]).into(bind.imgPic)
            Glide.with(requireActivity()).load(ShortCut_To.getBodyEquipDraw()[a]).into(bind.imgPic)

            if (a % 2 == 0) {
                binding.linLeft.addView(view)
            } else {
                binding.linRight.addView(view)
            }
            bind.cardMain.setOnClickListener {
                (activity as MainActivity).selectedCat = "Category"
                (activity as MainActivity).selected = ShortCut_To.getEquip()[a]
                (activity as MainActivity).navTo(List(), "Body", storage.fragValPrev!!, 1)
            }

        }

    }

    override fun onResume() {
        (activity as MainActivity).binding.txtTopic.text = "Equipment"
        super.onResume()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Main.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}