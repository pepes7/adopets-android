package com.example.adopets.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.*
import java.text.SimpleDateFormat


class CadastroEtapasActivity : AppCompatActivity() {

    private lateinit var stateProgressBar: StateProgressBar
    private lateinit var btn_continuar: Button
    private lateinit var btn_voltar: Button
    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var email: String
    private lateinit var nome: String
    private lateinit var senha: String
    private lateinit var dados: Bundle

    private lateinit var database : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_etapas)

        dados = intent.extras
        email = dados.getString("email")
        nome = dados.getString("nome")
        senha = dados.getString("senha")

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        stateProgressBar = findViewById(R.id.progresso)
        btn_continuar = findViewById(R.id.btn_continuar)
        btn_voltar = findViewById(R.id.btn_voltar)
        linear1 = findViewById(R.id.step1)
        linear2 = findViewById(R.id.step2)

        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

        //verifica se a barra de progresso esta ativa
        if (progresso != null) {

            btn_continuar.setOnClickListener() {

                //se a barra estiver na etapa 1, tornara a etapa 2 desativada

                if (stateProgressBar.currentStateNumber == 1) {

                    if(confirma(1)) {
                        btn_voltar.visibility = View.VISIBLE

                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)

                        linear1.visibility = View.GONE
                        linear2.visibility = View.VISIBLE
                    }

                } else if (progresso.currentStateNumber == 2) {
                    confirma(2)
                }
            }

            btn_voltar.setOnClickListener() {

                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

                linear1.visibility = View.VISIBLE
                linear2.visibility = View.GONE

                btn_voltar.visibility = View.GONE
            }

        }
    }


//verifica campos conforme a etapa
    fun confirma(etapa: Int): Boolean{

        var tel = telefone.text.toString()
        var dataN = dataNasc.text.toString()

        if(etapa == 1){

            if (tel.isEmpty()) {
                telefone.error = "Campo obrigatório!"
                return false
            }

            if (dataN.isEmpty()) {
                dataNasc.error = "Campo obrigatório!"
                return false
            }

        } else if(etapa == 2){
            var comp = complemento.text.toString()
            var ruaR = rua.text.toString()
            var bairroR = bairro.text.toString()
            var cepR = cep.text.toString()
            var numeroR = numero.text.toString()

            if (comp.isEmpty()) {
                complemento.error = "Campo obrigatório!"
                return false
            } else if (ruaR.isEmpty()) {
                rua.error = "Campo obrigatório!"
                return false
            } else if (cepR.isEmpty()) {
                cep.error = "Campo obrigatório!"
                return false
            } else if(bairroR.isEmpty()) {
                bairro.error = "Campo Obrigatório"
                return false
            } else if(numeroR.isEmpty()){
                numero.error = "Campo Obrigatório"
                return false
            } else {
                //firebase
                val u = Usuario()
                u.email = email
                u.nome = nome
                u.senha = senha
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                u.dataNasc = formatter.parse(dataN)
                //foto
                u.telefone = tel
                u.bairro = bairroR
                u.rua = ruaR
                u.numero = numeroR
                u.complemento = comp
                u.cep = cepR

                val usuarios = database.child("usuarios")
                val ref = usuarios.push()

                auth.createUserWithEmailAndPassword(u.email, senha)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        ref.child("email").setValue(u.email)
                        ref.child("nome").setValue(u.nome)
//                        ref.child("dataNasc").setValue(u.dataNasc)
                        //foto
                        ref.child("telefone").setValue(u.telefone)
                        ref.child("bairro").setValue(u.bairro)
                        ref.child("rua").setValue(u.rua)
                        ref.child("numero").setValue(u.numero)
                        ref.child("complemento").setValue(u.complemento)
                        ref.child("cep").setValue(u.cep)
                        Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            Toast.makeText(
                                this@CadastroEtapasActivity,
                                "Senha fraca!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(
                                this@CadastroEtapasActivity,
                                "Email inválido!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: FirebaseAuthUserCollisionException) {
                            Toast.makeText(
                                this@CadastroEtapasActivity,
                                "Usuário já cadastrado!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@CadastroEtapasActivity,
                                "" + e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                   }
            }

            }
        }

        return true
    }

}



//   val visibility = if (linear2.visibility == View.VISIBLE) View.GONE else View.VISIBLE

