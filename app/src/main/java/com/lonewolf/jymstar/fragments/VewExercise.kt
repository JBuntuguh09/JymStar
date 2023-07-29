package com.lonewolf.jymstar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.adapters.RecyclerMyExercises
import com.lonewolf.jymstar.adapters.RecyclerViewEx
import com.lonewolf.jymstar.databinding.FragmentVewExerciseBinding
import com.lonewolf.jymstar.resources.ShortCut_To
import com.lonewolf.jymstar.resources.Storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VewExercise.newInstance] factory method to
 * create an instance of this fragment.
 */
class VewExercise : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentVewExerciseBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    var selected = ArrayList<HashMap<String, String>>()
    var path: MainActivity? = null

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
        val view =  inflater.inflate(R.layout.fragment_vew_exercise, container, false)
        binding = FragmentVewExerciseBinding.bind(view)

        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(requireContext())
        path = (activity as MainActivity)

        path!!.binding.imgLogin.visibility = View.VISIBLE
        path!!.binding.imgLogin.setOnClickListener {
            val decode = ShortCut_To.decodeBase64(path!!.selectedExercise["QR_Code"])
            val message = "Hello, check out my custom Workout Plan on JymStar. Enter " +
                    "${path!!.selectedExercise["Code"]} or scan the attached QR Code"
            ShortCut_To.sendToWhatsapp(message, decode!!, requireContext())
        }



        if((activity as MainActivity).selectedEx.size==0){
            getData((activity as MainActivity).selectedFireRoute)
        }else {
            selected = (activity as MainActivity).selectedEx
            setData()
        }
        return view
    }

    private fun getData(path:String) {
        databaseReference.child(path).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                selected.clear()
                for(son in snapshot.children){
                    val hashs = HashMap<String, String>()
                    hashs["Position"] = son.child("Position").value.toString()
                    hashs["Rep"] = son.child("Reps").value.toString()
                    hashs["Name"] = son.child("Name").value.toString()
                    hashs["Time"] = son.child("Time").value.toString()
                    hashs["Url"] = son.child("Url").value.toString()
                    hashs["Count"] = son.child("Count").value.toString()

                    if(son.child("Equipment").exists()){
                        hashs["Equipment"] = son.child("Equipment").value.toString()
                    }else{
                        hashs["Equipment"] = ""
                    }

                    if(son.child("Body Part").exists()){
                        hashs["Body Part"] = son.child("Body Part").value.toString()
                    }else{
                        hashs["Body Part"] = ""
                    }
                    if(son.child("MainId").exists()){
                        hashs["MainId"] = son.child("MainId").value.toString()
                    }else{
                        hashs["MainId"] = ""
                    }

                    selected.add(hashs)
                }

                if(selected.size>0){
                    setData()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setData() {

        try {
            binding.progressBar.visibility = View.GONE
            val recyclerViewEx = RecyclerViewEx(requireContext(), selected)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            binding.recycler.layoutManager = linearLayoutManager
            binding.recycler.itemAnimator = DefaultItemAnimator()
            binding.recycler.adapter = recyclerViewEx
        }catch (e:Exception){
            binding.progressBar.visibility = View.GONE
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VewExercise.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VewExercise().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}