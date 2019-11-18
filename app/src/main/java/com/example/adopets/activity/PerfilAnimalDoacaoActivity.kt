package com.example.adopets.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import com.example.adopets.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil_animal_doacao.*

class PerfilAnimalDoacaoActivity : AppCompatActivity() {

    private lateinit var btn_adotar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_animal_doacao)

        btn_adotar = findViewById(R.id.adotar)

        btn_adotar.setOnClickListener {
            startActivity(Intent(this, FormularioActivity::class.java))
        }

        outra_resposta.setOnClickListener{
            outraResposta()
        }

        btn_respostaDoador.setOnClickListener{
            respostaDoador()
        }

        inicializar()
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

    fun inicializar(){
        var data = intent.extras

        nome_perfil_animal_adocao.text = data.getString("nome")
        raca_perfil_animal_adocao.text = data.getString("raca")
        endereco_perfil_animal_adocao.text = data.getString("bairro")
        tamanho_perfil_animal_adocao.text = data.getString("tamanho")
        tipo_perfil_animal_adocao.text  = data.getString("tipo")
        sexo_perfil_animal_adocao.text = data.getString("sexo")
        necessidade_perfil_animal_adocao.text = data.getString("necessidade")
        dataNasc_perfil_animal_adocao.text = data.getString("dataNasc")
        descricao_perfil_animal_adocao.text = data.getString("descricao")

        Picasso.get()
            .load(data.getString("foto"))
            .into(foto_perfil_animal_adocao)

    }

}


