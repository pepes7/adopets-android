package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adopets.R
import com.example.adopets.model.Animal
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_perfil_animal.*

class PerfilAnimalActivity : AppCompatActivity() {
    private lateinit var imagemPerfil : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_animal)
        inicializar()
    }

    fun inicializar(){
        var data = intent.extras
        Picasso.get()
            .load(data.getString("foto"))
            .into(foto_perfil_animal)
        nome_perfil_animal.text = data.getString("nome")
        raca_perfil_animal.text = data.getString("raca")
        tipo_perfil_animal.text  = data.getString("tipo")

        //chamar telas Dados Pet e Listagem Candidatos Adocao
      /*



        endereco_perfil_animal.text = data.getString("bairro")
        necessidade_perfil_animal.text = data.getString("necessidade")
        dataNasc_perfil_animal.text = data.getString("dataNasc")
        descricao_perfil_animal.text = data.getString("descricao")
*/

    }


}


