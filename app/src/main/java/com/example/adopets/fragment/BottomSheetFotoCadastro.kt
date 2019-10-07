package com.example.adopets.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.adopets.R
import kotlinx.android.synthetic.main.bottom_sheet_foto_cadastro.*
import kotlinx.android.synthetic.main.bottom_sheet_foto_cadastro.view.*
import java.lang.ClassCastException

class BottomSheetFotoCadastro : BottomSheetDialogFragment(){
    lateinit var mListener : BottomSheetListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.bottom_sheet_foto_cadastro,container,false)

        v.btn_galeria_foto.setOnClickListener{
            mListener.onButtonClicked( R.id.btn_galeria_foto)
            dismiss()

        }

        v.btn_camera_foto.setOnClickListener{
            mListener.onButtonClicked( R.id.btn_camera_foto)
            dismiss()
        }

        return v
    }

    interface BottomSheetListener{
        fun onButtonClicked(id: Int)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            mListener  = context as BottomSheetListener
        }catch (e:ClassCastException){
            throw ClassCastException("${context.toString()} must implement BottomSheetListener")

        }



    }
}