package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.adopets.R
import com.example.adopets.adapter.CandidatosAdapter
import com.example.adopets.model.Formulario
import com.example.adopets.model.Usuario
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_listagem_candidatos_adocao.*
import java.util.*

class ListagemCandidatosAdocaoActivity : AppCompatActivity() {

    private var usuarios = arrayListOf<Usuario>()
    private var formularios = arrayListOf<Formulario>()
    private lateinit var usuariosRecuperados : DatabaseReference
    private lateinit var formulariosRecuperados : DatabaseReference
    private lateinit var adapterCandidato: CandidatosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem_candidatos_adocao)

        usuariosRecuperados =  FirebaseDatabase.getInstance().reference.child("usuarios")

        formulariosRecuperados =  FirebaseDatabase.getInstance().reference.child("formulario")


        recyclerViewTodosAdotantes.layoutManager = LinearLayoutManager(this)
        recyclerViewTodosAdotantes.hasFixedSize()

        adapterCandidato = CandidatosAdapter(this, usuarios,formularios)

        recyclerViewTodosAdotantes.adapter = adapterCandidato

        //recupera dados
        recuperarFormulario()


    }

    fun recuperarFormulario(){
        var data = intent.extras

        formulariosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usuarios.clear()
                formularios.clear()
                for (d in dataSnapshot.children){
                    val u = d.getValue(Formulario::class.java)

                    //verifica o id do animal e pega o id do candidato
                    if(u!!.idAnimal.equals( data!!.getString("id"))){
                        formularios.add(u!!)
                        exibirCandidato(u.idAdotante)
                    }

                }
                Collections.reverse(formularios)
            }

        })


    }

    fun exibirCandidato(id : String){
        usuariosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    val u = d.getValue(Usuario::class.java)


                    //verifica o id do usuario
                    if(u!!.id.equals( id)){
                        usuarios.add(u)
                    }

                }

                Collections.reverse(usuarios)
                adapterCandidato.notifyDataSetChanged()
            }

        })


    }
}
