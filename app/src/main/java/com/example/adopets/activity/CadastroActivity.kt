package com.example.adopets.activity

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

    private lateinit var database : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    fun cadastrar(view: View) {

//        var nome = editTextNome.text.toString()
//        var email = editTextEmail.text.toString()
//        var senha = editTextSenha.text.toString()
//
//        var valido = true
//
//        if (nome.isEmpty()) {
//            editTextNome.error = "Campo obrigatório!"
//            valido = false
//        }
//
//        if (email.isEmpty()) {
//            editTextEmail.error = "Campo obrigatório!"
//            valido = false
//        }
//
//        if (senha.isEmpty()) {
//            editTextSenha.error = "Campo obrigatório!"
//            valido = false
//        }
//
//        if (valido) {
//            val usuarios = database.child("usuarios")
//            val u = Usuario()
//            u.email = email
//            u.nome = nome
//            val ref = usuarios.push()

//            auth.createUserWithEmailAndPassword(u.email, senha)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        ref.child("email").setValue(u.email)
//                        ref.child("nome").setValue(u.nome)
                        startActivity(Intent(applicationContext, CadastroEtapasActivity::class.java))
//                        Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT)
//                            .show()
//                    } else {
//                        try {
//                            throw task.exception!!
//                        } catch (e: FirebaseAuthWeakPasswordException) {
//                            Toast.makeText(
//                                this@CadastroActivity,
//                                "Senha fraca!",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } catch (e: FirebaseAuthInvalidCredentialsException) {
//                            Toast.makeText(
//                                this@CadastroActivity,
//                                "Email inválido!",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } catch (e: FirebaseAuthUserCollisionException) {
//                            Toast.makeText(
//                                this@CadastroActivity,
//                                "Usuário já cadastrado!",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } catch (e: Exception) {
//                            Toast.makeText(
//                                this@CadastroActivity,
//                                "" + e.message,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
                }
         //   }
        //}

    }
