package com.lonewolf.jymstar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.adapters.RecyclerMyExercises
import com.lonewolf.jymstar.databinding.FragmentWorkoutsBinding
import com.lonewolf.jymstar.resources.Storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Workouts.newInstance] factory method to
 * create an instance of this fragment.
 */
class Workouts : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentWorkoutsBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private val arrayList =ArrayList<HashMap<String, String>>()

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
        val view = inflater.inflate(R.layout.fragment_workouts, container, false)
        binding = FragmentWorkoutsBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference

        getWorkouts()
        getButtons()

        return view
    }

    private fun getWorkouts() {
        databaseReference.child("Workouts").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progressBar.visibility = View.GONE
                arrayList.clear()
                binding.recycler.removeAllViews()
                for (father in snapshot.children){
                    val hashMap = HashMap<String, String>()
                    hashMap["Name"] = father.child("Name").value.toString()
                    hashMap["Date"] = father.child("Date").value.toString()
                    hashMap["Code"] = father.child("Code").value.toString()
                    hashMap["Path"] = "My_Exercises/${storage.uSERID}/${father.key}/Body"

                    arrayList.add(hashMap)
                }
                if(arrayList.size>0){
                    try {
                        val recyclerMyExercises = RecyclerMyExercises(requireContext(), arrayList)
                        val linearLayoutManager = LinearLayoutManager(requireContext())
                        binding.recycler.layoutManager = linearLayoutManager
                        binding.recycler.itemAnimator = DefaultItemAnimator()
                        binding.recycler.adapter = recyclerMyExercises
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
            }

        })
    }

    private fun getButtons() {

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Workouts.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Workouts().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}