package com.example.adopets.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adopets.R
import com.example.adopets.model.AnimalAdotado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_candidato_adocao.*

class CandidatoAdocaoActivity : AppCompatActivity() {
    private lateinit var data : Bundle
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidato_adocao)

        data = intent.extras

        inicializar()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        aceitar.setOnClickListener{
            alerta()
        }
    }

    //recupera as informações do candidato
    fun inicializar(){
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

    fun alerta() {
        salvar()
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
        builder.setMessage("Agora você só precisa entrar em contato com o adotante para garantir a doação do animalzinho.")
        builder.setCancelable(false)
        builder.setPositiveButton("Ligar para adotante") { dialogInterface, i ->
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",data.getString("telefone"),null))
            startActivity(intent) }


        val dialog = builder.create()
        dialog.show()
    }

    fun salvar(){
        val adotado = AnimalAdotado()

        adotado.aceitou = "Espera"
        adotado.idAdotante = data.getString("idUsuario")
        adotado.idAnimal = data.getString("idAnimal")
        adotado.idDoador = auth!!.currentUser!!.uid

        val ref = database.child("animalAdotado").push()

        adotado.id = ref.key.toString()

        ref.setValue(adotado)
    }

}
