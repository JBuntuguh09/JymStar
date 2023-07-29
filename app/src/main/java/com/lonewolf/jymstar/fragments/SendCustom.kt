package com.lonewolf.jymstar.fragments


import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentSendCustomBinding
import com.lonewolf.jymstar.databinding.LayoutCustomRepBinding
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To
import net.glxn.qrgen.android.QRCode


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SendCustom.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendCustom : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentSendCustomBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private var selected: ArrayList<HashMap<String, String>>? = null

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
        val view = inflater.inflate(R.layout.fragment_send_custom, container, false)
        binding = FragmentSendCustomBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        selected = (activity as MainActivity).selectedEx

        getButtons()
        setEx()
        return view

    }

    private fun getButtons() {

        binding.btnSubmit.setOnClickListener {
            if(binding.edtName.text.toString().isEmpty()){
                binding.edtName.error = "Enter exercise title"
                binding.edtName.requestFocus()
            }else{
                val hashMap = HashMap<String, Any>()
                val code = "${ShortCut_To.timestamp}${ShortCut_To.generateRandomCode(4)}"
                val myBitmap: Bitmap = QRCode.from(code).bitmap()
                val base64 = ShortCut_To.convertBitmapToBase64(myBitmap)
                binding.imgPic.setImageBitmap(myBitmap)
                binding.txtCode.text = code
                hashMap["Code"] = code
                hashMap["Name"] = binding.edtName.text.toString()
                hashMap["Date"] = ShortCut_To.currentDateFormat2
                hashMap["Body"] = selected!!

                hashMap["Description"] = binding.edtDesc.text.toString()
                hashMap["QR_Code"] = base64

                databaseReference.child("Plans").child(code).setValue(hashMap)
                databaseReference.child("My_Exercises").child(storage.uSERID!!).child(code)
                    .setValue(hashMap).addOnSuccessListener {
                        databaseReference.child("Codes").child(code).setValue(storage.uSERID!!).addOnSuccessListener{
                            Toast.makeText(requireContext(), "Successfully created exercise", Toast.LENGTH_SHORT).show()
                           binding.textInputLayout.visibility = View.GONE
                            binding.textInputLayoutDesc.visibility = View.GONE

                            binding.linMain.visibility = View.GONE
                            binding.btnSubmit.visibility = View.GONE
                            binding.linSubmitted.visibility = View.VISIBLE

                            binding.btnQRShareCode.setOnClickListener {
                                ShortCut_To.sendToWhatsapp("Hello use $code to access my custom workout plan" +
                                        "on JymStar", myBitmap, requireContext())
                            }

                            binding.btnShareCode.setOnClickListener {
                                ShortCut_To.sendMessToWhatsApp("Hello use $code to access my custom workout plan" +
                                        "on JymStar", requireContext())
                            }


                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
            }
        }

    }

    private fun setEx(){
        val layoutInflater = LayoutInflater.from(requireContext())
        for(a in 0 until selected!!.size){
            val hashMap = selected!![a]
            val view = layoutInflater.inflate(R.layout.layout_custom_rep, binding.linMain, false)
            val bind = LayoutCustomRepBinding.bind(view)
            bind.txtName.text = hashMap["Name"]
            Glide.with(requireContext()).load(hashMap["Url"]).into(bind.imgPic)

            bind.edtCount.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    hashMap["Count"] = p0.toString()
                    selected!![a] = hashMap

                }

            })

            bind.edtRep.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    hashMap["Reps"] = p0.toString()
                    selected!![a] = hashMap
                }

            })

            bind.edtMins.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    hashMap["Time"] = p0.toString()
                    selected!![a] = hashMap
                }

            })
            binding.linMain.addView(view)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SendCustom.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SendCustom().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}