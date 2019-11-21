package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adopets.R
import com.example.adopets.model.Animal
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_dados_pet.*

class DadosPetActivity : AppCompatActivity() {
    private lateinit var imagemPerfil : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_animal)
        inicializar()
    }

    fun inicializar(){
        var data = intent.extras

        nome_perfil_animal.text = data.getString("nome")
        raca_perfil_animal.text = data.getString("raca")
        endereco_perfil_animal.text = data.getString("bairro")
        tamanho_perfil_animal.text = data.getString("tamanho")
        tipo_perfil_animal.text  = data.getString("tipo")
        sexo_perfil_animal.text = data.getString("sexo")
        necessidade_perfil_animal.text = data.getString("necessidade")
        dataNasc_perfil_animal.text = data.getString("dataNasc")
        descricao_perfil_animal.text = data.getString("descricao")

        Picasso.get()
            .load(data.getString("foto"))
            .into(foto_perfil_animal)

    }


}


