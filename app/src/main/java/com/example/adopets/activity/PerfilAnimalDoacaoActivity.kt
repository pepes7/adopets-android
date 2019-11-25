package com.example.adopets.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import com.example.adopets.R
import com.example.adopets.model.AnimalAdotado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil_animal_doacao.*

class PerfilAnimalDoacaoActivity : AppCompatActivity() {

    private lateinit var btn_adotar: Button
    private lateinit var data : Bundle
    private lateinit var escolhido : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_animal_doacao)

        btn_adotar = findViewById(R.id.adotar)
        data = intent.extras
        auth = FirebaseAuth.getInstance()

        btn_adotar.setOnClickListener {
            val intent = Intent(this, FormularioActivity::class.java)
            intent.putExtra("id", data.getString("id"))

            startActivity(intent)
        }

        outra_resposta.setOnClickListener{
            outraResposta()
        }


        escolhido = FirebaseDatabase.getInstance().reference.child("animalAdotado")
        recuperarFormulario()

        inicializar()
    }


    fun recuperarFormulario(){

        escolhido.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    val u = d.getValue(AnimalAdotado::class.java)


                    if(u!!.idAdotante.equals(auth!!.currentUser!!.uid ) &&u!!.idAnimal.equals(data.getString("id"))){
                        val drawable = getDrawable(R.drawable.ic_notifications_yellow_24dp)
                        btn_respostaDoador.background = drawable
                        btn_respostaDoador.setOnClickListener{
                            respostaDoador()
                        }
                    }

                }
            }

        })


    }

    //na tabela processo
    fun respostaDoador(){
        val builder = AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("PARABÉNS!")
        builder.setMessage("Você foi escolhido como novo dono deste pet")
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


