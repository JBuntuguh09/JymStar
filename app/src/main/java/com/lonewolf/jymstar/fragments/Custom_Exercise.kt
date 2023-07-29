package com.lonewolf.jymstar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.adapters.RecyclerWorkout
import com.lonewolf.jymstar.database.ExerciseViewModel
import com.lonewolf.jymstar.database.Exercises
import com.lonewolf.jymstar.databinding.FragmentCustomExerciseBinding
import com.lonewolf.jymstar.databinding.LayoutPartsBinding
import com.lonewolf.jymstar.resources.ShortCut_To
import com.lonewolf.jymstar.resources.Storage


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Custom_Exercise.newInstance] factory method to
 * create an instance of this fragment.
 */
class Custom_Exercise : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentCustomExerciseBinding
    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
    private  val arrayList = ArrayList<HashMap<String, String>>()
    private  val arrayListSelect = ArrayList<HashMap<String, String>>()
    private lateinit var exerciseViewModel: ExerciseViewModel
    

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
        val view = inflater.inflate(R.layout.fragment_custom__exercise, container, false)
        binding = FragmentCustomExerciseBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        exerciseViewModel = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory)[ExerciseViewModel::class.java]
        exerciseViewModel.liveData.observe(viewLifecycleOwner){ data->
            if (data.isNotEmpty()) {
                getOffLine(data)
            } else {
                getData()
            }
        }
       
        getButttons()
        return view
    }

    private fun getOffLine(data: List<Exercises>) {
        binding.progressBar.visibility = View.GONE
        arrayList.clear()
        binding.recycler.removeAllViews()
        for(element in data) {
            val hasd = element
            val hashMap = HashMap<String, String>()
            hashMap["Name"] = hasd.name
            hashMap["Url"] = hasd.url
            hashMap["Equipment"] = hasd.equipment
            hashMap["Target"] = hasd.target
            hashMap["MainId"] = hasd.mainId
            hashMap["Body Part"] = hasd.bodyPart
            hashMap["MainId"] = hasd.mainId


            arrayList.add(hashMap)
        }
        if(arrayList.size>0){
            val recycler = RecyclerWorkout(requireContext(), arrayList, arrayListSelect,  binding)
            // val mLinearLayoutManager = LinearLayoutManager(requireContext())
            val mLinearLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            binding.recycler.layoutManager = mLinearLayoutManager
            binding.recycler.itemAnimator = DefaultItemAnimator()
            binding.recycler.adapter = recycler
            binding.recycler.visibility = View.VISIBLE
        }
    }

    private fun getButttons() {
        binding.btnConfirm.setOnClickListener {
            (activity as MainActivity).selectedEx = arrayListSelect
            (activity as MainActivity).navTo(SendCustom(), "Confirm Details", storage.currPage!!, 1)
        }

        binding.edtSearch.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.edtSearch.right - binding.edtSearch.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {

                   search(binding.edtSearch.text.toString().uppercase())
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    fun setHori(hashMap: HashMap<String, Any>){
        val layoutInflater = LayoutInflater.from(requireContext())


        val view = layoutInflater.inflate(R.layout.layout_parts, binding.linMain, false)
        val bind = LayoutPartsBinding.bind(view)
        Glide.with(requireActivity()).load(hashMap["Url"]).into(bind.imgPic)
        bind.txtPart.text = hashMap["Name"].toString()

        binding.linMain.addView( view)
        binding.horiMain.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun search(word:String){
        binding.recycler.removeAllViews()
        binding.progressBar.visibility = View.VISIBLE
        val newArray = ArrayList<HashMap<String, String>>()
        for(a in 0 until arrayList.size){
            val hashMap = arrayList[a]
            if(hashMap["Name"]!!.uppercase().contains(word) || hashMap["Target"]!!.uppercase().contains(word)
                || hashMap["Equipment"]!!.uppercase().contains(word) || hashMap["Body Part"]!!.uppercase().contains(word)){
                newArray.add(arrayList[a])
            }
            ShortCut_To.hideKeyboard(requireActivity())
            binding.txtNoExercise.visibility = View.GONE
            if(newArray.size>0){
                val recycler = RecyclerWorkout(requireContext(), newArray, arrayListSelect,  binding)
                val mLinearLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                binding.recycler.layoutManager = mLinearLayoutManager
                binding.recycler.itemAnimator = DefaultItemAnimator()
                binding.recycler.adapter = recycler
                binding.recycler.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

            }else{
                binding.recycler.removeAllViews()
                binding.progressBar.visibility = View.GONE
                binding.txtNoExercise.visibility = View.VISIBLE
            }
        }
    }

    private fun getData() {
        binding.progressBar.visibility = View.GONE
        databaseReference.child("Main_Exercises").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progressBar.visibility = View.GONE
                arrayList.clear()
                binding.recycler.removeAllViews()
                for( father in snapshot.children){
                    val hashMap = HashMap<String, String>()
                    hashMap["Name"] = father.child("Name").value.toString()
                    hashMap["Target"] = father.child("Target").value.toString()
                    hashMap["Url"] = father.child("Url").value.toString()
                    hashMap["Equipment"] = father.child("Equipment").value.toString()
                    hashMap["Body Part"] = father.child("Body Part").value.toString()
                    hashMap["MainId"] = father.child("id").value.toString()


                    arrayList.add(hashMap)
                }
                println(arrayList)
                if(arrayList.size>0){
                  //  binding.progressBar.visibility = View.GONE
                    insertData()

                }else {
                    binding.recycler.removeAllViews()
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
            val exercise = Exercises(0, hash["Name"]?:"None", hash["Url"]?:"None",
            hash["Equipment"]?:"None", hash["Target"]?:"None", hash["Body Part"]?:"None", hash["MainId"]?:"None")
            exerciseViewModel.insert(exercise)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Custom_Exercise.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Custom_Exercise().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}