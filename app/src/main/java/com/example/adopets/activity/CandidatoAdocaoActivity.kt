package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adopets.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_candidato_adocao.*

class CandidatoAdocaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidato_adocao)

        inicializar()
    }

    //recupera as informações do candidato
    fun inicializar(){
        var data = intent.extras

        //informações do candidato
        Picasso.get()
            .load(data.getString("foto"))
            .into(imagePerfilAdotante)

        nome.text = data.getString("nome")
        telefone.text = data.getString("telefone")
        email.text  = data.getString("email")

        //informações do formulario
        tipo_residencia.text = data.getString("pgtResidencia")
        qtdePessoas.text = data.getString("pgtPessoasMoram")
        qtdeAnimais.text = data.getString("pgtAnimaisCasa")
        pet_de_guarda.text = data.getString("pgtProtegerFamilia")
        local_sozinho.text = data.getString("pgtOndeTempo")
        tempo_sozinho.text = data.getString("pgtQuantoTempo")
    }

}
