package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.example.adopets.R
import com.example.adopets.model.Usuario
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.*

class CadastroEtapasActivity : AppCompatActivity() {

    val stateProgressBar: StateProgressBar = findViewById(R.id.progresso)
    val btn_continuar: Button = findViewById(R.id.btn_continuar)
    val btn_voltar: Button = findViewById(R.id.btn_voltar)
    val linear1: LinearLayout = findViewById(R.id.step1)
    val linear2: LinearLayout = findViewById(R.id.step2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_etapas)

        //verifica se a barra de progresso esta ativa
        if (progresso != null) {

            btn_continuar.setOnClickListener() {

                //se a barra estiver na etapa 1, tornara a etapa 2 desativada

                if (stateProgressBar.currentStateNumber == 1) {
                    btn_voltar.visibility = View.GONE

                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

                    linear2.visibility = View.GONE
                    linear1.visibility = View.VISIBLE

                    confirma(1)

                } else if (progresso.currentStateNumber == 2) {
                    btn_voltar.visibility = View.VISIBLE
                    confirma(2)
                }
            }

            btn_voltar.setOnClickListener() {

                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)

                linear1.visibility = View.GONE
                linear2.visibility = View.VISIBLE


            }

        }
    }


//verifica campos conforme a etapa
    fun confirma(etapa: Int){
        var valido = true

        if(etapa == 1){

            var tel = telefone.text.toString()
            var dataN = dataNasc.text.toString()

            if (tel.isEmpty()) {
                telefone.error = "Campo obrigatório!"
                valido = false
            }

            if (dataN.isEmpty()) {
                dataNasc.error = "Campo obrigatório!"
                valido = false
            }

        } else if(etapa == 2){
            var comp = complemento.text.toString()
            var ruaR = rua.text.toString()
            var bairroR = bairro.text.toString()
            var cepR = cep.text.toString()

            if (comp.isEmpty()) {
                complemento.error = "Campo obrigatório!"
                valido = false
            }

            if (ruaR.isEmpty()) {
                rua.error = "Campo obrigatório!"
                valido = false
            }

            if (cepR.isEmpty()) {
                cep.error = "Campo obrigatório!"
                valido = false
            }

            if(bairroR.isEmpty()){
                bairro.error = "Campo Obrigatório"
                valido = false
            }
        }
        if(valido){
        //firebase

        }
    }

}



//   val visibility = if (linear2.visibility == View.VISIBLE) View.GONE else View.VISIBLE

