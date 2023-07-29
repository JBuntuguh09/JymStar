package com.lonewolf.jymstar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lonewolf.jymstar.databinding.ActivityLoginBinding
import com.lonewolf.jymstar.resources.Constant
import com.lonewolf.jymstar.resources.Storage

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            // setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(this)
        setContentView(binding.root)

        getButtons()
    }

    private fun getButtons() {

        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            if(binding.edtEmail.text.toString().isEmpty()){
                binding.edtEmail.error = getString(R.string.enter_your_email)
                binding.edtEmail.requestFocus()
            }else if (!binding.edtEmail.text.toString().contains("@")){
                binding.edtEmail.error = getString(R.string.entervalidemail)
                binding.edtEmail.requestFocus()
            }else if (binding.edtPassword.text.toString().isEmpty()){
                binding.edtPassword.error = getString(R.string.enter_your_password)
                binding.edtPassword.requestFocus()
            }else{
                binding.progressBar.visibility = View.VISIBLE
                loginTo()
            }
        }
    }

    private fun loginTo() {
        auth.signInWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
            .addOnSuccessListener {
                storage.uSERID  = it.user!!.uid.toString()
                auth.signInWithEmailAndPassword(Constant.username, Constant.password)
                    .addOnSuccessListener {
                        databaseReference.child("Users").child(storage.uSERID!!).addListenerForSingleValueEvent(
                            object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    storage.fULLNAME = snapshot.child("Name").value.toString()
                                    storage.email = snapshot.child("Email").value.toString()
                                    finish()

                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            }
                        )
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}