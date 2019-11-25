package com.example.adopets.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.adopets.R
import com.example.adopets.adapter.AnimalAdapter
import com.example.adopets.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_doados.*
import java.util.*


class DoadosFragment : Fragment() {
    private lateinit var btn_dismiss: Button
    private lateinit var recyclerViewAnimais: RecyclerView
    private var animais = arrayListOf<Animal>()
    private lateinit var adapterAnimal: AnimalAdapter
    private lateinit var animaisRecuperados : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_doados, container, false)

        btn_dismiss = view.findViewById(R.id.dismiss)
        btn_dismiss.setOnClickListener {
            cardView2.visibility = View.GONE

        }
        auth = FirebaseAuth.getInstance()

        animaisRecuperados =  FirebaseDatabase.getInstance().reference.child("animal")

        recyclerViewAnimais = view.findViewById(R.id.recyclerViewAnimais)
        recyclerViewAnimais.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewAnimais.hasFixedSize()

        adapterAnimal = AnimalAdapter(context!!,animais,"doacao")

        recyclerViewAnimais.adapter = adapterAnimal

        //recupera dados
        recuperarAnimal()
        return view
    }

    // fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
    //   savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    // return inflater!!.inflate(R.layout.fragment_doados, container, false)
    //}



    private fun recuperarAnimal(){
        animaisRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                animais.clear()
                for (d in dataSnapshot.children){
                    val u = d.getValue(Animal::class.java)

                    //verifica se o usuário atual fez a doaçaõ
                    if(u!!.doador.equals(auth!!.currentUser!!.uid)){
                        animais.add(u!!)
                    }


                }

                Collections.reverse(animais)
                adapterAnimal.notifyDataSetChanged()
            }

        })

    }

    private fun animais(): List<Animal> {

        var query: Query =  FirebaseDatabase.getInstance().reference.child("animais")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Passar os dados para a interface grafica
                for (snapshot in dataSnapshot.getChildren()) {
                    val animal = snapshot.getValue(Animal::class.java!!)
                    println(animal?.nome)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Se ocorrer um erro
            }
        })

        return listOf(
            Animal("Tots", "Macho", "Japiim"),
            Animal("Mel", "Fêmea", "São Jorge"),
            Animal("Ferrer", "Macho", "Alvorada")
        )
    }

}
