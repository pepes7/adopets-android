package com.example.adopets.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.fragment.PerfilAnimalFragment
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


    }



    //configura as etapas do cadastro
    fun verificaEtapa(){

            btn_continuar.setOnClickListener() {

                //se a barra estiver na etapa 1, tornara a etapa 2 desativada

                if (linear1.visibility == View.VISIBLE) {

                    if(confirma(1)) {
                        btn_voltar.visibility = View.VISIBLE
                        btn_continuar.visibility = View.VISIBLE

              //progresso = 2
                        linear1.visibility = View.GONE
                        linear2.visibility = View.VISIBLE
                    }

                } else if (linear2.visibility == View.VISIBLE) {
                    btn_continuar.visibility = View.GONE
                    btn_finalizar.visibility = View.VISIBLE

                    if(confirma(2)){
                        openDialog()
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

    fun openDialog(){
//abrir fragment tipo atividade
        btn_finalizar.setOnClickListener {

           val builder = AlertDialog.Builder(applicationContext)
                with(builder) {
                    setTitle("O que você deseja fazer com este animal?")
                    setPositiveButton("Adotar", null)
                    setNegativeButton("Ajudar", null)
                    setNeutralButton("Cancelar", null)
                }

                val alertDialog = builder.create()
                alertDialog.show()

                val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                with(button) {
                    setPadding(0, 0, 20, 0)
                    setTextColor(Color.RED)
                }

                val button2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                with(button2) {
                    setPadding(0, 0, 40, 0)
                    setTextColor(Color.BLUE)
                }

                val button3 = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                with(button3) {
                    setPadding(0, 0, 20, 0)
                    setTextColor(Color.DKGRAY)
                }

            }}


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
}






