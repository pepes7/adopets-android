package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.adopets.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

    }

    fun cadastrar(view: View){
        //auth.createUserWithEmailAndPassword(editTextEmail.text.toString(),editTextSenha.text.toString())

        //database.child("pepesTeste").setValue("Teste 1")

    }
}
