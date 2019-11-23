package com.example.adopets.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    fun verMaisDados(view:View){
        var data = intent.extras

        val intent = Intent(this,DadosPetActivity::class.java)
        intent.putExtra("nome",data.getString("nome"))
        intent.putExtra("sexo", data.getString("sexo"))
        intent.putExtra("bairro",data.getString("bairro"))
        intent.putExtra("id", data.getString("id"))
        intent.putExtra("foto",data.getString("foto"))
        intent.putExtra("situacao", data.getString("situacao"))
        intent.putExtra("raca", data.getString("raca"))
        intent.putExtra("descricao",data.getString("descricao"))
        intent.putExtra("tamanho",data.getString("tamanho"))
        intent.putExtra("necessidade", data.getString("necessidade"))
        intent.putExtra("tipo",data.getString("tipo"))
        intent.putExtra("dataNasc",data.getString("dataNasc"))

        startActivity(intent)
    }

    fun candidados(view:View){
        var data = intent.extras
        val intent = Intent(this,ListagemCandidatosAdocaoActivity::class.java)
        intent.putExtra("id", data.getString("id"))
        startActivity(intent)
    }



}


