package com.example.adopets.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.adopets.R
import com.example.adopets.activity.CandidatoAdocaoActivity
import com.example.adopets.model.Formulario
import com.example.adopets.model.Usuario
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class CandidatosAdapter(private val context: Context, private val listCandidatos: ArrayList<Usuario>, private val listaFormulario : ArrayList<Formulario>,private val idAnimal : String) : RecyclerView.Adapter<CandidatosAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.interessados_adotar,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listCandidatos.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val usuario = listCandidatos.get(i)
        myViewHolder.nome.text = usuario.nome
        myViewHolder.bairro.text = usuario.bairro

        val formulario = listaFormulario.get(i)




        //metodo de click

        myViewHolder.itemView.setOnClickListener {
            val intent = Intent(context, CandidatoAdocaoActivity::class.java)

            //dados do adotante
            intent.putExtra("nome",usuario.nome)
            intent.putExtra("foto",usuario.foto)
            intent.putExtra("telefone",usuario.telefone)
            intent.putExtra("email",usuario.email)
            intent.putExtra("idUsuario",usuario.id)
            intent.putExtra("idAnimal",idAnimal)

            //dados do seu formulario
            intent.putExtra("pgtResidencia", formulario.pgtResidencia)
            intent.putExtra("pgtPessoasMoram",formulario.pgtPessoasMoram)
            intent.putExtra("pgtAnimaisCasa", formulario.pgtAnimaisCasa)
            intent.putExtra("pgtOndeTempo",formulario.pgtOndeTempo)
            intent.putExtra("pgtQuantoTempo",formulario.pgtQuantoTempo)
            intent.putExtra("pgtProtegerFamilia",formulario.pgtProtegerFamilia)



            context.startActivity(intent)
        }

        //pega a primeira imagem da lista
        Picasso.get()
            .load(usuario?.foto)
            .into(myViewHolder.foto)

    }

    inner  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nome : TextView
        var bairro : TextView
        var foto : CircleImageView

        init {
            nome = itemView.findViewById(R.id.nome_candidato)
            bairro = itemView.findViewById(R.id.bairro_candidato)
            foto = itemView.findViewById(R.id.image_candidato)

        }
    }




}
