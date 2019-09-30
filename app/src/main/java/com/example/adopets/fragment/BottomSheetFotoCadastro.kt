package com.example.adopets.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adopets.R

class BottomSheetFotoCadastro : BottomSheetDialogFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
       val v = inflater.inflate(R.layout.bottom_sheet_foto_cadastro,container,false)

        return v
    }
}