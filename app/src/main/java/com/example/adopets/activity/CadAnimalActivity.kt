package com.example.adopets.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.model.Usuario
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_cad_animal.*
import kotlinx.android.synthetic.main.activity_cadastro_etapas.*
import kotlinx.android.synthetic.main.activity_cadastro_etapas.btn_continuar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.btn_voltar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.dataNasc

class CadAnimalActivity : AppCompatActivity() {


    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_animal)
        linear1 = findViewById(R.id.step1)
        linear2 = findViewById(R.id.step2)


        verificaEtapa()
        btn_finalizar.setOnClickListener{
            situacaoProcessoAnimal()
        }


    }



    //configura as etapas do cadastro
    fun verificaEtapa(){

            btn_continuar.setOnClickListener() {

                //se a barra estiver na etapa 1, tornara a etapa 2 desativada

                if (linear1.visibility == View.VISIBLE) {
                    btn_continuar.visibility = View.VISIBLE
                    if(confirma(1)) {
                        btn_continuar.visibility = View.VISIBLE
                        btn_finalizar.visibility = View.GONE
                        btn_voltar.visibility = View.VISIBLE


              //progresso = 2
                        linear1.visibility = View.GONE
                        btn_continuar.visibility = View.GONE
                        btn_finalizar.visibility = View.VISIBLE
                        linear2.visibility = View.VISIBLE
                    }

                } else if (linear2.visibility == View.VISIBLE) {


                    if(confirma(2)){
                       btn_finalizar.setOnClickListener{
                           situacaoProcessoAnimal()

                       }
                      //  startActivity(Intent(this, PerfilAnimalActivity::class.java))
                    }
                }
            }

            btn_voltar.setOnClickListener() {

//progresso = 1
                linear1.visibility = View.VISIBLE
                linear2.visibility = View.GONE

                btn_voltar.visibility = View.GONE
            }

    }



    //verifica campos conforme a etapa
    fun confirma(etapa: Int): Boolean{

        var nom = nome.text.toString()
        var rac = raca.text.toString()
        var tam = tamanho.text.toString()
      //sexo e' um radio button


        if(etapa == 1){

            if (nom.isEmpty()) {
                nome.error = "Campo obrigatório!"
                return false
            }

            if (tam.isEmpty()) {
                tamanho.error = "Campo obrigatório!"
                return false
            }

        } else if(etapa == 2){

            //pre visu de animal na etapa 2
            nome2.text = nom

            //restantes inputs

            //falta o tipo, ele e' radiobutton, so ele e obrigatorio
            var neces = necessidade.text.toString()
            var desc = descricao.text.toString()
            var dataN = dataNasc.text.toString()
            /*

            if (tip.isEmpty()) {
                tipo.error = "Campo obrigatório!"
                return false
            }*/
        }

        return true
    }

    //na tabela processo
    fun situacaoProcessoAnimal(){
        val builder = android.support.v7.app.AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Este animal precisa...")
      //  builder.setMessage("Para a melhor utilização do app é necessário aceitar as permissões")
        builder.setCancelable(false)
      //  builder.setNegativeButton("Receber Ajuda")
        builder.setPositiveButton("Ser Doado" ){ dialogInterface, i ->
            dialogMotivoDoacao()
        }



        val dialog = builder.create()
        dialog.show()
        //motivo
        //se quer doaç respos e explica
    }

    fun dialogMotivoDoacao(){

        val builder = android.support.v7.app.AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Por que você quer doá-lo?")
        //TextInput com id motivo para adicionar na tabela doacao


       // builder.setMessage("Para a melhor utilização do app é necessário aceitar as permissões")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar" ){ dialogInterface, i ->
            escolhaAdocaoResponsavel()
        }



        val dialog = builder.create()
        dialog.show()
    }

    fun escolhaAdocaoResponsavel(){
        val builder = android.support.v7.app.AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Você quer realizar uma doação responsável?")


        builder.setMessage("É uma forma de feedback para você receber formulários de respostas dos candidatos e escolher o adotante ideal para o seu pet!")
        builder.setCancelable(false)
        //salvar escolha do usuário
        builder.setPositiveButton("Sim" ){ dialogInterface, i ->  }




        val dialog = builder.create()
        dialog.show()
    }
}






