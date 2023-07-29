package com.lonewolf.jymstar.fragments.equipment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.adapters.RecyclerExercise
import com.lonewolf.jymstar.adapters.RecyclerWorkout
import com.lonewolf.jymstar.database.Details
import com.lonewolf.jymstar.database.DetailsViewModel
import com.lonewolf.jymstar.database.Exercises
import com.lonewolf.jymstar.databinding.FragmentListBinding
import com.lonewolf.jymstar.resources.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [List.newInstance] factory method to
 * create an instance of this fragment.
 */
class List : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding : FragmentListBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var detailsViewModel: DetailsViewModel
    private  val arrayList = ArrayList<HashMap<String, String>>()

    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        binding = FragmentListBinding.bind(view)
        storage = Storage(requireContext())
        linearLayoutManager = LinearLayoutManager(requireContext())

        databaseReference = FirebaseDatabase.getInstance().reference

        detailsViewModel = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory).get(DetailsViewModel::class.java)


        getData()

        getButton()

        return view
    }

    private  fun getData() {
        detailsViewModel.getEquipment((activity as MainActivity).selected).observe(viewLifecycleOwner){ data->
            if (data.isNotEmpty()) {
                getOffLine(data)
            } else {
                getList()
            }
        }
    }

    private fun getOffLine(data: kotlin.collections.List<Details>) {

        for(element in data){
            val hashMap = HashMap<String, String>()
            hashMap["Category"] = element.category
            hashMap["details"] = element.details
            hashMap["Difficulty"] = element.difficulty
            hashMap["Force"] = element.force
            hashMap["Grips"] = element.details
            hashMap["exercise_name"] = element.exerciseName
            hashMap["steps"] = element.steps
            hashMap["target"] = element.target
            hashMap["videoURL"] = element.videoUrl
            hashMap["youtubeURL"] = element.youtubeURL
            hashMap["id"] = element.id

            hashMap["muscle"] = "Default"

            arrayList.add(hashMap)
        }
        if(arrayList.size>0){
            try {
                binding.progressBar.visibility = View.GONE
                val recyclerExercise = RecyclerExercise(requireContext(), arrayList)
                //val mLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

                binding.recycler.layoutManager = linearLayoutManager
                binding.recycler.itemAnimator = DefaultItemAnimator()
                binding.recycler.adapter = recyclerExercise
                binding.recycler.visibility = View.VISIBLE
            }catch (e : Exception){
                e.printStackTrace()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun getButton() {
        binding.edtSearch.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {

                    search(binding.edtSearch.text.toString())
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun search(word:String){
        binding.recycler.removeAllViews()
        binding.progressBar.visibility = View.VISIBLE
        val newArray = ArrayList<HashMap<String, String>>()
        for(a in 0 until arrayList.size){
            val hashMap = arrayList[a]
            if(hashMap["exercise_name"]!!.contains(word) || hashMap["Category"]!!.contains(word)
                || hashMap["target"]!!.contains(word) || hashMap["Body Part"]!!.contains(word)){
                newArray.add(arrayList[a])
            }

            if(newArray.size>0){
                binding.progressBar.visibility = View.GONE
                val recyclerExercise = RecyclerExercise(requireContext(), newArray)
                binding.recycler.layoutManager = linearLayoutManager
                binding.recycler.itemAnimator = DefaultItemAnimator()
                binding.recycler.adapter = recyclerExercise
                binding.recycler.visibility = View.VISIBLE
            }
        }
    }
    private fun getList() {
        println((activity as MainActivity).selectedCat+" // "+(activity as MainActivity).selected)
        databaseReference.child((activity as MainActivity).selectedCat).child((activity as MainActivity).selected)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.recycler.removeAllViews()
                    for( father in snapshot.children){
                        val hashMap = HashMap<String, String>()
                        hashMap["Category"] = father.child("Category").value.toString()
                        hashMap["details"] = father.child("details").value.toString()
                        hashMap["Difficulty"] = father.child("Difficulty").value.toString()
                        hashMap["Force"] = father.child("Force").value.toString()
                        hashMap["Grips"] = father.child("Grips").value.toString()
                        hashMap["exercise_name"] = father.child("exercise_name").value.toString()
                        hashMap["steps"] = father.child("steps").value.toString()
                        hashMap["target"] = father.child("target").value.toString()
                        hashMap["videoURL"] = father.child("videoURL").value.toString()
                        hashMap["youtubeURL"] = father.child("youtubeURL").value.toString()
                        hashMap["id"] = father.child("id").value.toString()

                        hashMap["muscle"] = "Default"
                        val target =  father.child("target").value.toString()
                        if(target.isNotEmpty() && target!="None"){
                            val tObject = JSONObject(target)
                            try {
                                val prim :String =  tObject["Primary"].toString()
                                val stringArray = Gson().fromJson(prim, Array<String>::class.java)
                                hashMap["muscle"] = stringArray[0]
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        arrayList.add(hashMap)
                    }
                    if(arrayList.size>0){
                        insertData()

                    }else {
                        binding.recycler.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.progressBar.visibility = View.GONE
                }

            })
    }

    private fun insertData(){
        for(a in 0 until arrayList.size){
            val hash = arrayList[a]

            val details = Details(0, hash["exercise_name"]!!, hash["Category"]!!,
                hash["details"]!!, hash["Difficulty"]!!, hash["Force"]!!, hash["Grips"]!!,
                hash["steps"]!!, hash["target"]!!, hash["videoURL"]!!, hash["youtubeURL"]!!,hash["id"]!!
            )
            detailsViewModel.insert(details)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment List.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            List().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}