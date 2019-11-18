package com.example.adopets.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.adopets.R
import com.example.adopets.activity.PerfilAnimalActivity
import com.example.adopets.model.Animal
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_view_animais.view.*
import java.util.ArrayList

class AnimalAdapter(private val context: Context, private val listAnimais: ArrayList<Animal>) : RecyclerView.Adapter<AnimalAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_animais,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listAnimais.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val animal = listAnimais.get(i)
        myViewHolder.nome.text = animal.nome
        myViewHolder.sexo.text = animal.sexo
        myViewHolder.bairro.text = animal.bairro


        //metodo de click
        myViewHolder.itemView.setOnClickListener {
            val intent = Intent(context, PerfilAnimalActivity::class.java)

            intent.putExtra("nome",animal.nome)
            intent.putExtra("sexo",animal.sexo)
            intent.putExtra("bairro",animal.bairro)
            intent.putExtra("id", animal.id)
            intent.putExtra("foto",animal.foto)
            intent.putExtra("situacao", animal.situacao)
            intent.putExtra("raca",animal.raca)
            intent.putExtra("descricao",animal.descricao)
            intent.putExtra("tamanho",animal.tamanho)
            intent.putExtra("necessidade",animal.necessidade)
            intent.putExtra("tipo",animal.tipo)
            intent.putExtra("dataNasc",animal.dataNasc)
            intent.putExtra("doador",animal.doador)
            

            context.startActivity(intent)
        }

        //pega a primeira imagem da lista
        Picasso.get()
            .load(animal?.foto)
            .into(myViewHolder.foto)

    }

    inner  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nome : TextView
        var sexo : TextView
        var bairro : TextView
        var foto : CircleImageView

        init {
            nome = itemView.findViewById(R.id.txt_nome_list)
            sexo = itemView.findViewById(R.id.txt_sexo_list)
            bairro = itemView.findViewById(R.id.txt_bairro_list)
            foto = itemView.findViewById(R.id.imagem_animal_list)

        }
    }




}

