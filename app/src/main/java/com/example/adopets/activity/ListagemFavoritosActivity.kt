package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import com.example.adopets.R
import com.example.adopets.adapter.AnimalAdapter2
import com.example.adopets.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_listagem_favoritos.*
import java.util.*

class ListagemFavoritosActivity : AppCompatActivity() {

    private var animais = arrayListOf<Animal>()
    private lateinit var adapterAnimal: AnimalAdapter2
    private lateinit var favoritos : DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem_favoritos)

        auth = FirebaseAuth.getInstance()
        favoritos =  FirebaseDatabase.getInstance().reference.child("usuarios").child(auth!!.currentUser!!.uid).child("favoritos")



        recyclerViewAnimaisFavoritos.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewAnimaisFavoritos.hasFixedSize()

        adapterAnimal = AnimalAdapter2(this,animais,auth!!.currentUser!!.uid)

        recyclerViewAnimaisFavoritos.adapter = adapterAnimal

        //recupera dados
        recuperarAnimal()

    }

    private fun recuperarAnimal(){
        favoritos.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                animais.clear()
                for (d in dataSnapshot.children){
                    val u = d.getValue(Animal::class.java)

                    //verifica se o usuário atual fez a doaçaõ
                    if(!u!!.doador.equals(auth!!.currentUser!!.uid)){
                        animais.add(u!!)
                    }


                }

                Collections.reverse(animais)
                adapterAnimal.notifyDataSetChanged()
            }

        })

    }
}
