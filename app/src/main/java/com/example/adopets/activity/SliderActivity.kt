package com.example.adopets.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.example.adopets.R
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import kotlinx.android.synthetic.main.intro_4.*

class SliderActivity : IntroActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //remove os botões de avançar e voltar
        isButtonBackVisible = true
        buttonBackFunction = BUTTON_BACK_FUNCTION_BACK
        isButtonNextVisible = true
        isButtonCtaVisible = false



        //método para adicionar slider
        addSlide(FragmentSlide.Builder()
            .background(R.color.colorBackground)
            .fragment(R.layout.intro_1)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.colorBackground)
            .fragment(R.layout.intro_2)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.colorBackground)
            .fragment(R.layout.intro_3)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.colorBackground)
            .fragment(R.layout.intro_4)
            .canGoForward(false)
            .build())

        buttonCtaClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(Intent(baseContext,MainActivity::class.java))
            }

        }



//        addSlide(FragmentSlide.Builder()
//            .background(R.color.colorBackground)
//            .fragment(R.layout.activity_main)
//            .canGoForward(false)
//            .build())




    }

    fun trocarTela(view:View){
        startActivity(Intent(baseContext,MainActivity::class.java))
    }
}

