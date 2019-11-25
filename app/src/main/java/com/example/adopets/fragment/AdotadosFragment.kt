package com.example.adopets.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.adapter.AnimalAdapter
import com.example.adopets.model.Animal
import com.example.adopets.model.Formulario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_adotados.*
import kotlinx.android.synthetic.main.fragment_adotados.view.*
import java.util.*




class AdotadosFragment : Fragment() {
    private lateinit var btn_dismiss: Button
    private lateinit var animaisRecuperados : DatabaseReference
    private lateinit var adapterAnimal: AnimalAdapter
    private var animais = arrayListOf<Animal>()
    private var formularios = arrayListOf<Formulario>()
    private lateinit var auth: FirebaseAuth
    private lateinit var formulariosRecuperados : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_adotados, container, false)
        btn_dismiss = view.findViewById(R.id.dismiss)
        btn_dismiss.setOnClickListener {
            cardView2.visibility = View.GONE

        }

        view.pendencias.setOnClickListener{
            Toast.makeText(context,"AAA",Toast.LENGTH_SHORT).show()
            animais.clear()

            adapterAnimal = AnimalAdapter(context!!,animais,"adocao")
            view.recyclerViewAnimaisAdotados.adapter = adapterAnimal
        }


        auth = FirebaseAuth.getInstance()
        animaisRecuperados =  FirebaseDatabase.getInstance().reference.child("animal")
        formulariosRecuperados =  FirebaseDatabase.getInstance().reference.child("formulario")


        view.recyclerViewAnimaisAdotados.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        view.recyclerViewAnimaisAdotados.hasFixedSize()

        adapterAnimal = AnimalAdapter(context!!,animais,"adocao")

        view.recyclerViewAnimaisAdotados.adapter = adapterAnimal

        recuperarFormulario()

        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun exibirAnimal(idAnimal : String){
        animaisRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    val u = d.getValue(Animal::class.java)

                    //verifica se o usuário atual fez a doaçaõ
                    if(u!!.id.equals(idAnimal)){
                        animais.add(u!!)
                    }


                }

                Collections.reverse(animais)
                adapterAnimal.notifyDataSetChanged()
            }

        })

    }

    fun recuperarFormulario(){

        formulariosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                animais.clear()
                formularios.clear()
                for (d in dataSnapshot.children){
                    val u = d.getValue(Formulario::class.java)

                    //verifica o id do animal e pega o id do candidato
                    if(auth!!.currentUser!!.uid.equals(u!!.idAdotante)){
                        formularios.add(u!!)
                        exibirAnimal(u!!.idAnimal)
                    }

                }
                Collections.reverse(formularios)
            }

        })


    }

}
