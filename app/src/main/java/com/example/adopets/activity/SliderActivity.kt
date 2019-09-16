package com.example.adopets.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.adopets.R
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class SliderActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //remove os botões de avançar e voltar
        isButtonBackVisible = false
        isButtonNextVisible = false
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
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.colorBackground)
            .fragment(R.layout.intro_5)
            .canGoForward(false)
            .build())



    }

    fun trocarTela(view:View){
        startActivity(Intent(baseContext,MainActivity::class.java))
    }
}
