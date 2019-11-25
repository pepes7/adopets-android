package com.example.adopets.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.adopets.R
import kotlinx.android.synthetic.main.fragment_doados.*

class AdotadosFragment : Fragment() {
    private lateinit var btn_dismiss: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_adotados, container, false)
        btn_dismiss = view.findViewById(R.id.dismiss)
        btn_dismiss.setOnClickListener {
            cardView2.visibility = View.GONE

        }
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

}
