package com.example.adopets.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adopets.R
import com.example.adopets.model.Animal
import kotlinx.android.synthetic.main.list_view_animais.view.*

class AnimalAdapter(private val animais: List<Animal>,  private val context: Context) : Adapter<AnimalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_view_animais, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return animais.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val animal = animais[p1]
        p0.nome.text = animal.nome
//        p0.sexo.text = animal.sexo
//        p0.bairro.text = animal.bairro
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome = itemView.txt_nome_list
//        val sexo = itemView.txt_sexo_list
//        val bairro = itemView.txt_bairro_list

        fun bindView(animal: Animal) {
            val nome = itemView.txt_nome_list
//            val sexo = itemView.txt_sexo_list
//            val bairro = itemView.txt_bairro_list

            nome.text = animal.nome
//            sexo.text = animal.sexo
//            bairro.text = animal.bairro
        }
    }

}

