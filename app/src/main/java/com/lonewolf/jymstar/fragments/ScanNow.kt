package com.lonewolf.jymstar.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.lonewolf.jymstar.MainActivity
import com.lonewolf.jymstar.R
import com.lonewolf.jymstar.databinding.FragmentScanNowBinding
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To
import com.squareup.picasso.Picasso


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScanNow.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanNow : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentScanNowBinding
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
        val view = inflater.inflate(R.layout.fragment_scan_now, container, false)
        binding = FragmentScanNowBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        getBttons()
        onButtonClick()

        return view
    }

    private fun getBttons() {
        binding.btnScanCode.setOnClickListener {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt("Scan QR Code")
            options.setCameraId(0) // Use a specific camera of the device
            options.setBeepEnabled(false)
            options.setBarcodeImageEnabled(false)

            firstCheck(barcodeLauncher.launch(options))

        }

        binding.btnSubmitCode.setOnClickListener {
            if(binding.edtCode.text.toString().isEmpty()){
                binding.edtCode.error = getString(R.string.entercode)
            }else{
                getMyCode(binding.edtCode.text.toString())
            }
        }

        binding.btnScanCodeFromPic.setOnClickListener {
            firstCheck(pictureSelectionLauncher.launch("image/*"))
        }

        binding.btnScanImage.setOnClickListener {
           val code = ShortCut_To.scanQRCodeFromImageView(binding.imageView2)
            if(code==null){
                Toast.makeText(requireContext(), "Invalid Image", Toast.LENGTH_SHORT).show()
                binding.btnScanImage.visibility = View.GONE
            }else{
                getMyCode(code)
            }

        }

    }

    private fun getMyCode(code:String) {
        binding.progressBar.visibility = View.VISIBLE
        val arrayList = ArrayList<HashMap<String, String>>()
        databaseReference.child("Codes").child(code).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference.child("My_Exercises").child(snapshot.value.toString()).child(code)
                    .addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                           binding.progressBar.visibility = View.GONE
                            val name =  snapshot.child("Name").value.toString()
                            for (father in snapshot.child("Body").children){
                                val hashMap = HashMap<String, String>()
                                hashMap["Position"] = father.child("Position").value.toString()
                                hashMap["Rep"] = father.child("Rep").value.toString()
                                hashMap["Name"] = father.child("Name").value.toString()
                                hashMap["Time"] = father.child("Time").value.toString()
                                hashMap["Url"] = father.child("Url").value.toString()
                                hashMap["Count"] = father.child("Count").value.toString()
                                hashMap["Name"] = name

                                arrayList.add(hashMap)
                            }

                            if(arrayList.size>0){
                                (activity as MainActivity).selectedEx = arrayList
                                (activity as MainActivity).navTo(VewExercise(), name, storage.currPage!!, 1)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Launch
    private fun onButtonClick() {

    }

    private val pictureSelectionLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedUri ->
            Picasso.get().load(uri).into(binding.imageView2)
            binding.btnScanImage.visibility = View.VISIBLE

           // println(code)
        }
    }

    private fun firstCheck(launch: Unit) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Ask for permision
            Log.d("Permission Granted", "No")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            launch
            Log.d("Permission Granted", "Yes")

// Permission has already been granted
        }
    }



    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //startQRScanning()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }



    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanNow.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanNow().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}