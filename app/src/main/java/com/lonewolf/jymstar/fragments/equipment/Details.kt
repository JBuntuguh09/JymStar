package com.lonewolf.jymstar.fragments.equipment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentDetailsBinding
import com.lonewolf.jymstar.databinding.LayoutSmallPartBinding
import com.lonewolf.jymstar.databinding.LayoutStepsBinding
import com.lonewolf.jymstar.fragments.Video
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Details.newInstance] factory method to
 * create an instance of this fragment.
 */
class Details : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentDetailsBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference


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
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        binding = FragmentDetailsBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference

        setData((activity as MainActivity).selectedExercise)
        return view
    }

    private fun setData(hashMap: HashMap<String, String>) {

//


//        Picasso.get().load(ShortCut_To.getEquipbyEx(hashMap["Category"]!!)).into(binding.imgPic)
        binding.txtEquip.text = hashMap["Category"]
        binding.txtName.text = hashMap["exercise_name"]
        if(hashMap["Difficulty"].equals("Beginner")){
            binding.ratings.numStars = 1
        }else if(hashMap["Difficulty"].equals("Intermediate")){
            binding.ratings.numStars = 2
        }else if(hashMap["Difficulty"].equals("Advanced")){
            binding.ratings.numStars = 3
        }else {
            binding.ratings.numStars = 0
        }
        if(!hashMap["details"].equals("None")){
            binding.txtDetail.text = hashMap["details"]
        }else{
            binding.txtDetail.visibility = View.GONE
            binding.txtDetailLabel.visibility = View.GONE

        }

        if(!hashMap["videoURL"].equals("None") && hashMap["videoURL"]!!.isNotEmpty()){
            val vidz = Gson().fromJson(hashMap["videoURL"], Array<String>::class.java)
            val layoutInflater = LayoutInflater.from(requireContext())
            for (c in vidz.indices){
                val viewz = layoutInflater.inflate(R.layout.layout_small_part, binding.linVidz, false)
                val bindo = LayoutSmallPartBinding.bind(viewz)
                Glide.with(requireActivity()).load(R.drawable.place_vid).override(40, 40).into(bindo.imgPic)
                bindo.txtName.visibility = View.GONE

                bindo.linPlay.setOnClickListener {
                    (activity as MainActivity).selectedPath = vidz[c]
                    (activity as MainActivity).navTo(Video(), "Play", storage.fragValPrev!!, 1)
                }
                binding.linVidz.addView(viewz)
                if(c==0){
                    val videoUrl = vidz[c]

                    val mediaController = android.widget.MediaController(requireContext())
                    mediaController.setAnchorView(binding.videoView)
                    binding.videoView.setMediaController(mediaController)

                    val videoUri = Uri.parse(videoUrl)
                    binding.videoView.setVideoURI(videoUri)
                   // binding.videoView.start()
                }
            }
        }

        if(!hashMap["youtubeURL"].equals("None") && hashMap["youtubeURL"]!!.isNotEmpty()){
            //val vidz = Gson().fromJson(hashMap["youtubeURL"], Array<String>::class.java)
            val layoutInflater = LayoutInflater.from(requireContext())

                val viewz = layoutInflater.inflate(R.layout.layout_small_part, binding.linVidz, false)
                val bindo = LayoutSmallPartBinding.bind(viewz)
                Glide.with(requireActivity()).load(R.drawable.youtube).override(40, 40).into(bindo.imgPic)
                bindo.txtName.visibility = View.GONE

            bindo.linPlay.setOnClickListener {
                (activity as MainActivity).selectedPath = hashMap["youtubeURL"]!!
                (activity as MainActivity).navTo(Video(), "Youtube", storage.fragValPrev!!, 1)
            }
                binding.linVidz.addView(viewz)
        }

        if(!hashMap["steps"].equals("None") && hashMap["steps"]!!.isNotEmpty()){
            val steps = Gson().fromJson(hashMap["steps"], Array<String>::class.java)
            val layoutInflater = LayoutInflater.from(requireContext())
            for (c in steps.indices){
                val view = layoutInflater.inflate(R.layout.layout_steps, binding.linSteps, false)
                val bind = LayoutStepsBinding.bind(view)
                bind.txtNum.text = "${c+1}. "
                bind.txtStep.text = steps[c]

                binding.linSteps.addView(view)
            }
        }

        if(!hashMap["target"].equals("None") && hashMap["target"]!!.isNotEmpty()){
            val target =  hashMap["target"]
            val targets = ArrayList<String>()
            val layoutInflater = LayoutInflater.from(requireContext())

            if(target!!.isNotEmpty() && target!="None"){
                val tObject = JSONObject(target)
                try {
                    val prim :String =  tObject["Primary"].toString()
                    val stringArray = Gson().fromJson(prim, Array<String>::class.java)
                    for (b in stringArray.indices){
                        targets.add(stringArray[b])
                        val viewz = layoutInflater.inflate(R.layout.layout_small_part, binding.linTarget, false)
                        val bindo = LayoutSmallPartBinding.bind(viewz)
                        Glide.with(requireActivity()).load(ShortCut_To.getImageFromPart(stringArray[b])).into(bindo.imgPic)

                        bindo.txtName.text = stringArray[b]
                        binding.linTarget.addView(viewz)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

                try {
                    val prim :String =  tObject["Secondary"].toString()
                    val stringArray = Gson().fromJson(prim, Array<String>::class.java)
                    for (b in stringArray.indices){
                        targets.add(stringArray[b])
                        val viewz = layoutInflater.inflate(R.layout.layout_small_part, binding.linTarget, false)
                        val bindo = LayoutSmallPartBinding.bind(viewz)
                        Glide.with(requireActivity()).load(ShortCut_To.getImageFromPart(stringArray[b])).into(bindo.imgPic)

                        bindo.txtName.text = stringArray[b]
                        binding.linTarget.addView(viewz)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }



            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Details.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Details().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}