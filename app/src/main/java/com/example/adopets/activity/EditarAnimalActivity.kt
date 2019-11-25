package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adopets.R
import kotlinx.android.synthetic.main.activity_editar_animal.*

class EditarAnimalActivity : AppCompatActivity() {
    private lateinit var dados : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_animal)

        dados = intent.extras

        inicializar()
    }
//
//    intent.putExtra("nome",data.getString("nome"))
//    intent.putExtra("sexo",data.getString("sexo"))
//    intent.putExtra("bairro",data.getString("bairro"))
//    intent.putExtra("id", data.getString("id"))
//    intent.putExtra("foto",data.getString("foto"))
//    intent.putExtra("situacao", data.getString("situacao"))
//    intent.putExtra("raca",data.getString("raca"))
//    intent.putExtra("descricao",data.getString("descricao"))
//    intent.putExtra("tamanho",data.getString("tamanho"))
//    intent.putExtra("necessidade",data.getString("necessidade"))
//    intent.putExtra("tipo",data.getString("tipo"))
//    intent.putExtra("dataNasc",data.getString("dataNasc"))
//    intent.putExtra("doador",data.getString("doador"))

    fun inicializar(){


    }
}
