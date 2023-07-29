package com.lonewolf.jymstar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.jymstar.databinding.ActivityRegisterBinding
import com.lonewolf.jymstar.resources.Constant
import com.lonewolf.jymstar.resources.Storage
import com.lonewolf.jymstar.resources.ShortCut_To

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = Storage(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        getButtons()
    }

    private fun getButtons() {

        binding.btnSubmit.setOnClickListener {
            ShortCut_To.hideKeyboard(this)
            if (binding.edtName.text.toString().isEmpty()){
                binding.edtName.error = getString(R.string.enteryourname)
                binding.edtName.requestFocus()
            }else if (binding.edtEmail.text.toString().isEmpty()){
                binding.edtEmail.error = getString(R.string.enter_your_email)
                binding.edtEmail.requestFocus()
            }else if (!binding.edtEmail.text.toString().contains("@")){
                binding.edtEmail.error = getString(R.string.entervalidemail)
                binding.edtEmail.requestFocus()
            }else if (binding.edtPassword.text.toString().isEmpty()){
                binding.edtPassword.error = getString(R.string.enter_your_password)
                binding.edtPassword.requestFocus()
            }else if (binding.edtPassword.text.toString().length<7){
                binding.edtPassword.error = "Password should be 8 characters or more"
                binding.edtPassword.requestFocus()
            }else if(!binding.edtPassword.text.toString().equals(binding.edtConfirm.text.toString())){
                binding.edtConfirm.error = getString(R.string.passwordsdontmatch)
                binding.edtConfirm.requestFocus()
            }else{
                binding.progressBar2.visibility  = View.VISIBLE
                registerTo()
            }
        }
    }

    private fun registerTo() {
        auth.createUserWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPassword.text.toString()).
                addOnSuccessListener {
                    auth.signInWithEmailAndPassword(Constant.username, Constant.password).
                            addOnSuccessListener {
                                storage.uSERID = auth.currentUser!!.uid
                                val hashMap = HashMap<String, String>()
                                hashMap["Name"] = binding.edtName.text.toString()
                                hashMap["Email"] = binding.edtEmail.text.toString()
                                hashMap["CreatedDatetime"] = ShortCut_To.currentDateFormat2
                                hashMap["UserId"] = storage.uSERID!!
                                databaseReference.child("Users").child(storage.uSERID!!).setValue(hashMap).addOnSuccessListener {
                                    storage.fragValPrev = "Hello ${binding.edtName.text.toString()}"
                                    finish()
                                }
                            }.addOnFailureListener {
                        binding.progressBar2.visibility  = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
            binding.progressBar2.visibility  = View.GONE
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }
    }
}