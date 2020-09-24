package com.example.adopets.activity

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.model.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.util.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    fun cadastrar(view: View) {
        var camposVazios = ""

        var nome = editTextNome.text.toString()
        var email = editTextEmail.text.toString()
        var senha = editTextSenha.text.toString()

        var valido = true

        if (nome.isEmpty()) {
            editTextNome.error = "Campo obrigatório!"
            valido = false
            camposVazios += "Nome Completo; "
        }

        if (email.isEmpty()) {
            editTextEmail.error = "Campo obrigatório!"
            valido = false
            camposVazios += "E-mail; "
        }

        if (senha.isEmpty()) {
            editTextSenha.error = "Campo obrigatório!"
            valido = false
            camposVazios += "Senha."
        }

        if (camposVazios.equals("") && valido) {

            val u = Usuario()
            u.email = email
            u.nome = nome
            u.senha = senha

            intent = Intent(applicationContext, CadastroEtapasActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("nome", nome)
            intent.putExtra("senha", senha)
            startActivity(intent)

        } else {
            alertaCamposVazios(camposVazios)
        }

    }

    fun alertaCamposVazios(campos: String) {
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Ops...")
        builder.setMessage("Todos estes campos são obrigatórios, você tem que preencher: " + campos)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok, irei preencher") { dialogInterface, i -> }


        val dialog = builder.create()
        dialog.show()
    }


}
