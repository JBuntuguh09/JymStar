package com.lonewolf.jymstar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.jymstar.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setContentView(binding.root)
        getButtons()
    }

    private fun getButtons() {
        moveProg()
       // YoYo.with(Techniques.ZoomInDown).duration(3000).playOn(binding.ingLogo)
    }

    private fun moveProg(){
        val selTime = 6000

        val cDown= object : CountDownTimer(selTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
            }

            // When the task is over it will print 00:00:00 there
            override fun onFinish() {
                val intent = Intent(this@Splash, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        cDown.start()

    }
}