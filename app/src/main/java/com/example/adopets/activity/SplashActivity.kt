package com.example.adopets.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.adopets.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Para o Splash ocupar toda a tela do celular
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            Handler().postDelayed({

                startActivity(Intent(applicationContext, HomeActivity::class.java))
//            }, 5000)
//        } else {
//            Handler().postDelayed({
//                startActivity(Intent(baseContext, SliderActivity::class.java))
//            }, 5000)
//        }
    }
}
