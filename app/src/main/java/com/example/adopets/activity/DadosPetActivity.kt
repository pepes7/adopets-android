package com.example.adopets.activity

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.model.Animal
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_dados_pet.*

class DadosPetActivity : AppCompatActivity() {
    private lateinit var imagemPerfil : CircleImageView
    private lateinit var animal: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var data : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_pet)
        storageReference = FirebaseStorage.getInstance().reference
        inicializar()

        apagar_animal.setOnClickListener{
            alertaPermissao()
        }

        editar_animal.setOnClickListener{
            val intent = Intent(this, EditarAnimalActivity::class.java)

            intent.putExtra("nome",data.getString("nome"))
            intent.putExtra("sexo",data.getString("sexo"))
            intent.putExtra("bairro",data.getString("bairro"))
            intent.putExtra("id", data.getString("id"))
            intent.putExtra("foto",data.getString("foto"))
            intent.putExtra("situacao", data.getString("situacao"))
            intent.putExtra("raca",data.getString("raca"))
            intent.putExtra("descricao",data.getString("descricao"))
            intent.putExtra("tamanho",data.getString("tamanho"))
            intent.putExtra("necessidade",data.getString("necessidade"))
            intent.putExtra("tipo",data.getString("tipo"))
            intent.putExtra("dataNasc",data.getString("dataNasc"))
            intent.putExtra("doador",data.getString("doador"))

            startActivity(intent)
        }
    }

    fun inicializar(){
        data = intent.extras

        nome_perfil_animal.text = data.getString("nome")
        raca_perfil_animal.text = data.getString("raca")
        endereco_perfil_animal.text = data.getString("bairro")
        tamanho_perfil_animal.text = data.getString("tamanho")
        tipo_perfil_animal.text  = data.getString("tipo")
        sexo_perfil_animal.text = data.getString("sexo")
        necessidade_perfil_animal.text = data.getString("necessidade")
        dataNasc_perfil_animal.text = data.getString("dataNasc")
        descricao_perfil_animal.text = data.getString("descricao")

        Picasso.get()
            .load(data.getString("foto"))
            .into(foto_perfil_animal)

    }


    fun alertaPermissao() {
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Excluir animal")
        builder.setMessage("Você tem deseja que deseja excluir o animal?")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar") { dialogInterface, i -> apagarAnimal() }
        builder.setNegativeButton("Cancelar"){dialogInterface, i ->}


        val dialog = builder.create()
        dialog.show()
    }

    fun apagarAnimal(){
        animal =  FirebaseDatabase.getInstance().reference.child("animal").child(data.getString("id"))
        val imagemRef = storageReference
            .child("imagens")
            .child("animal")
            .child(data.getString("id"))
            .child("perfil.jpeg")

        imagemRef.delete().addOnFailureListener{
            Toast.makeText(this,"Erro na exclusão do animal",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            animal.removeValue().addOnSuccessListener {
                Toast.makeText(this,"Sucesso na exclusão do animal",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,HomeActivity::class.java))
            }.addOnFailureListener{
                Toast.makeText(this,"Erro na exclusão dos dados do animal",Toast.LENGTH_SHORT).show()
            }
        }

    }
}


