package com.lonewolf.jymstar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentSpinBinding
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Spin.newInstance] factory method to
 * create an instance of this fragment.
 */
class Spin : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentSpinBinding
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
        val view = inflater.inflate(R.layout.fragment_spin, container, false)
        binding = FragmentSpinBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        spinWheel()

        binding.button2.setOnClickListener {
            //spinWheel()
            var arr = ShortCut_To.getGifs()
            for ( a in arr.indices){
                var url: String
                println("Exercises/${arr[a]}.gif")
                    storageReference.child("Exercises/${arr[a]}.gif").downloadUrl.addOnSuccessListener {
                    url = it.toString()
                        println(url)
                        val hash = HashMap<String, String>()
                        hash["Url"] = url
                        hash["Name"] = arr[a].uppercase().replace("-", " ")
                        databaseReference.child("Workouts").child(arr[a].uppercase().replace("-", " ")).setValue(hash)
                        if(arr[a].lowercase().contains("pose") || arr[a].lowercase().contains("yoga")){
                            databaseReference.child("Yoga").child(arr[a].uppercase().replace("-", " ")).setValue(hash)
                        }

                        if(arr[a].lowercase().replace("-", " ").contains("push up") || arr[a].lowercase().contains("yoga")){
                            databaseReference.child("Push Up").child(arr[a].uppercase().replace("-", " ")).setValue(hash)
                        }
                }.addOnFailureListener {
                    println(it.message.toString())
                    }

            }

        }

        return view
    }

    private fun spinWheel() {
        var degree = ShortCut_To.getRand(0, 360).toFloat()
        val rotation = RotateAnimation(0f, degree+720,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f)

        rotation.duration = 2000
        rotation.fillAfter = true
        rotation.fillBefore = true
        rotation.interpolator = DecelerateInterpolator()

        rotation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                calculate(degree)
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })

        binding.imgWheel.startAnimation(rotation)
    }

    private fun calculate(degree: Float) {
        var list = arrayListOf("Push up", "Sit up", "Diamond Push up", "Burpees", "meter Run", "Squats",
        "Lunges", "Jumping Jacks", "Bicycle Crunch", "Mountain Climbers", "Jump Rope", "Stretches")
        var initial = 0
        var endPoint = 30
        var result : String? = null
        var a = 0

        do {
            if (degree > initial && degree < endPoint) {
                try {
                    result = list[a]
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            initial+=30
            endPoint+=30
            a++
        }while (result==null)

        println(list[a-1])
        Toast.makeText(requireContext(), list[a-1], Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Spin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Spin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}