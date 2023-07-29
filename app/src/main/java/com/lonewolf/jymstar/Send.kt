package com.lonewolf.jymstar

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.lonewolf.jymstar.databinding.FragmentSendBinding
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.utils.API
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Send.newInstance] factory method to
 * create an instance of this fragment.
 */
class Send : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentSendBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference
    val list = ArrayList<String>()

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
        val view = inflater.inflate(R.layout.fragment_send, container, false)

        binding = FragmentSendBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference

        getButtons()
        return view
    }

    private fun getButtons() {
        binding.button.setOnClickListener {
          //  binding.progressBar.visibility = View.VISIBLE
            println("hello")
fixit()
        //getCases()
        }

        binding.btnSubmit.setOnClickListener {
            getPath()
        }
    }

    private fun getPath() {
        val fileRef = storageRef.child("Main_Exercises/${binding.edtNumber.text.toString()}.gif")
        fileRef.downloadUrl.addOnSuccessListener{
            val url = it.toString()
            println("URL : $url")
            val hashMap = HashMap<String, String>()
            hashMap["Body Part"] = binding.edtBody.text.toString()
            hashMap["Equipment"] = binding.edtEquip.text.toString()
            hashMap["gifUrl"] = url
            hashMap["Url"] = url
            hashMap["Desc"] = binding.edtDesc.text.toString()
            hashMap["id"] = binding.edtNumber.text.toString()
            hashMap["Name"] = binding.edtName.text.toString()
            hashMap["Target"] = binding.edtTarget.text.toString()
            hashMap["Title"] = binding.edtName.text.toString().replace("-", " ")

            val num = binding.edtNumber.text.toString()
            databaseReference.child("Main_Exercises").child(num).setValue(hashMap).addOnSuccessListener {
                println("Done done Done")
            }.addOnFailureListener {
                println("Error ${it.message}")
            }

            databaseReference.child("Sub").child("Target").child(hashMap["Target"]!!).child(num).setValue(
                hashMap
            )
            databaseReference.child("Sub").child("Body Part").child(hashMap["Body Part"]!!).child(num).setValue(
                hashMap
            )
            databaseReference.child("Sub").child("Equipment").child(hashMap["Equipment"]!!).child(num).setValue(
                hashMap
            )

        }.addOnFailureListener {
            println("Failed : ${it.message}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getCases() {
       // binding.progressBar.visibility = View.VISIBLE

        val api = API()
        GlobalScope.launch {
            try {
                val res = api.getMuscleExercisesAPI("https://exercisedb.p.rapidapi.com/exercises",
                    "6f1121b31amsh09c0b8a1212bdfdp18ac72jsn592c77fcd053", "exercisedb.p.rapidapi.com")
                withContext(Dispatchers.Main){
                    storage.temp_1 = res
                    //setInfoOther(res)
                }
            }catch (e: Exception){
                e.printStackTrace()
//                binding.progressBar.visibility = View.GONE
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(), "No data found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
//1170 575
    }

    private fun setInfoOther(res: String) {
        val jsonArray = JSONArray(res)
        val list2 =ArrayList<String>()
        for (a in 0 until jsonArray.length()) {
            val hashMap = HashMap<String, String>()
            val jsonObject = jsonArray.getJSONObject(a)
            hashMap["Body Part"] = jsonObject.optString("bodyPart", "None")
            hashMap["Equipment"] = jsonObject.optString("equipment", "None")
            hashMap["gifUrl"] = jsonObject.optString("gifUrl", "None")
            hashMap["id"] = jsonObject.optString("id", "None")
            hashMap["Name"] = jsonObject.optString("name", "None")
            hashMap["Target"] = jsonObject.optString("target", "None")
            hashMap["Title"] = jsonObject.optString("name", "None").replace("-", " ")

            if(!list.contains(hashMap["id"]!!) ) {

                runTemp(hashMap["id"]!!, hashMap["gifUrl"]!!, hashMap)
                list2.add(hashMap["id"]!!)
            }
        }
        println("List 2 = ${list2.size}")

    }

    private fun setInfo(res: String) {


        val jsonArray = JSONArray(res)
        for (a in 0 until jsonArray.length()){
            val hashMap = HashMap<String, Any>()
            val jsonObject = jsonArray.getJSONObject(a)

            hashMap["details"] = jsonObject.optString("details", "None")
            hashMap["Category"] = jsonObject.optString("Category", "None")
            hashMap["Difficulty"] = jsonObject.optString("Difficulty", "None")
            hashMap["Force"] = jsonObject.optString("Force", "None")
            hashMap["Grips"] = jsonObject.optString("Grips", "None")
            hashMap["exercise_name"] = jsonObject.optString("exercise_name", "None")
            hashMap["steps"] = jsonObject.optString("steps", "None")
            hashMap["target"] = jsonObject.optString("target", "None")
            hashMap["videoURL"] = jsonObject.optString("videoURL", "None")
            hashMap["youtubeURL"] = jsonObject.optString("youtubeURL", "None")
            hashMap["id"] = jsonObject.optString("id", "None")

            println("$a // mmmm")
            databaseReference.child("Exercises").child(jsonObject.optString("exercise_name", "None"))
                .updateChildren(hashMap)
            databaseReference.child("Difficulty").child(jsonObject.optString("Difficulty", "None"))
                .child(jsonObject.getString("exercise_name")).updateChildren(hashMap)
            databaseReference.child("Force").child(jsonObject.optString("Force", "None"))
                .child(jsonObject.getString("exercise_name")).updateChildren(hashMap)
            databaseReference.child("Grips").child(jsonObject.optString("Grips", "None"))
                .child(jsonObject.getString("exercise_name")).updateChildren(hashMap)

            databaseReference.child("Category").child(jsonObject.optString("Category", "None"))
                .child(jsonObject.getString("exercise_name")).updateChildren(hashMap)

                val target =  jsonObject.optString("target", "None")
                if(target.isNotEmpty() && target!="None"){
                    val tObject = JSONObject(target)
                    try {
                        val prim :String =  tObject["Primary"].toString()
                        val stringArray = Gson().fromJson(prim, Array<String>::class.java)
                        for (b in stringArray.indices){
                            databaseReference.child("Muscle").child(stringArray[b])
                            .child(jsonObject.getString("exercise_name")).updateChildren(hashMap)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                    try {
                        val prim :String =  tObject["Secondary"].toString()
                        val stringArray = Gson().fromJson(prim, Array<String>::class.java)
                        for (b in stringArray.indices){
                            databaseReference.child("Muscle").child(stringArray[b])
                                .child(jsonObject.getString("exercise_name")).updateChildren(hashMap)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }



                }

        }

        binding.progressBar.visibility = View.GONE


    }

    private fun fixit(){
        databaseReference.child("Main_Exercises").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val arr = ArrayList<HashMap<String,String>>()


                for(father in snapshot.children){
                    val hashMap = HashMap<String, String>()
                    hashMap["Body Part"] = father.child("Body Part").value.toString()
                    hashMap["Equipment"] = father.child("Equipment").value.toString()
                    hashMap["gifUrl"] = father.child("gifUrl").value.toString()
                    hashMap["id"] = father.child("id").value.toString()
                    hashMap["Name"] = father.child("Name").value.toString()
                    hashMap["Target"] = father.child("Target").value.toString()
                    arr.add(hashMap)
                    if(father.child("Url").value.toString().contains("https://firebasestorage.googleapis.com/v0/b/jymstar-20ced.appspot.com")) {
                        list.add(hashMap["id"]!!)
                    }

                }
               // setInfoOther(storage.temp_1!!)
                //getCases()
                println("lisdd ${list.size}")
//                if (arr.size>0){
//                    for ( a in 0 until  arr.size){
//                        var url: String
//                        println("Main_Exercises/${arr[a]}.gif")
//                        storageRef.child("Main_Exercises/${arr[a]["id"]}.gif").downloadUrl.addOnSuccessListener {
//                            url = it.toString()
//
//                            val hash = HashMap<String, Any>()
//                            hash["Url"] = url
//                            hash["Title"] = arr[a]["Name"]!!.uppercase().replace("-", " ")
//
//                            databaseReference.child("Main_Exercises").child(arr[a]["id"]!!).updateChildren(hash)
//                            databaseReference.child("Sub").child("Target").child(arr[a]["Target"]!!).child(arr[a]["id"]!!).updateChildren(hash)
//                            databaseReference.child("Sub").child("Body Part").child(arr[a]["Body Part"]!!).child(arr[a]["id"]!!).updateChildren(hash)
//                            databaseReference.child("Sub").child("Equipment").child(arr[a]["Equipment"]!!).child(arr[a]["id"]!!).updateChildren(hash)
//
//                        }.addOnFailureListener {
//                            println(it.message.toString())
//                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//                        }
//
//                    }
//                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun runTemp(num:String, path:String, body: HashMap<String, String>){
       //  storageRef = FirebaseStorage.getInstance().reference

// Create a reference to the file in Firebase Storage
        val fileRef = storageRef.child("Main_Exercises/${num}.gif")

// Specify the URL you want to upload
        val urlString = path
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Create a temporary file to store the contents of the URL
                val tempFile = File.createTempFile("temp", null)

                // Download the contents of the URL and save it to the temporary file
                val url = URL(urlString)
                val inputStream = BufferedInputStream(url.openStream())
                val outputStream = FileOutputStream(tempFile)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                // Close the streams
                inputStream.close()
                outputStream.close()

                // Upload the temporary file to Firebase Storage
                val uploadTask = fileRef.putFile(Uri.fromFile(tempFile))

                // Listen for upload success or failure
                uploadTask.addOnSuccessListener {
                    // File upload successful
                    // You can get the download URL of the uploaded file
                    it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        body["Url"] = it.toString()

                        databaseReference.child("Main_Exercises").child(num).setValue(body)
                        databaseReference.child("Sub").child("Target").child(body["Target"]!!).child(num).setValue(body)
                        databaseReference.child("Sub").child("Body Part").child(body["Body Part"]!!).child(num).setValue(body)
                        databaseReference.child("Sub").child("Equipment").child(body["Equipment"]!!).child(num).setValue(body)

                        println(num)
                       // println(body["Url"])


                    }

                    // Do something with the download URL (e.g., save to database)
                }.addOnFailureListener { exception ->
                    // File upload failed
                    // Handle the failure (e.g., show an error message)
                    Log.e("TAG", "File upload failed: ${exception.message}")
                }
            } catch (exception: Exception) {
                // Handle any exceptions that occur during the process

                Log.e("TAG", "Error: ${exception}")
                exception.printStackTrace()
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
         * @return A new instance of fragment Send.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Send().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}