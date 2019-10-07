package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import com.example.adopets.R
import kotlinx.android.synthetic.main.activity_perfil_animal_doacao.*

class PerfilAnimalDoacaoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_animal_doacao)

        outra_resposta.setOnClickListener{
            outraResposta()
        }

        btn_respostaDoador.setOnClickListener{
            respostaDoador()
        }
    }

    //na tabela processo
    fun respostaDoador(){
        val builder = AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Espere mais um pouco!")
        builder.setMessage("O doador ainda não lhe escolheu como adotante deste pet")
        builder.setCancelable(false)
        //  builder.setNegativeButton("Receber Ajuda")
        builder.setPositiveButton("OK" ){ dialogInterface, i ->
            
        }



        val dialog = builder.create()
        dialog.show()
        //motivo
        //se quer doaç respos e explica
    }

    fun outraResposta(){
        val builder = AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Você foi selecionado!")
        builder.setMessage("Entre em contato com o doador por telefone ou e-mail para efetivar a adoção.")
        builder.setCancelable(false)
        //  builder.setNegativeButton("Receber Ajuda")
        builder.setPositiveButton("OK" ){ dialogInterface, i ->

        }



        val dialog = builder.create()
        dialog.show()

    }

}

