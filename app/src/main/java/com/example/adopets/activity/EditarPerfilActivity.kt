package com.example.adopets.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.adopets.R
import com.example.adopets.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import android.app.ProgressDialog
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var btn_editar: Button
    private lateinit var btn_voltar: Button

    private lateinit var database: DatabaseReference
    private lateinit var txt_nome: TextView
    private lateinit var txt_email: TextView
    private lateinit var txt_rua: TextView
    private lateinit var txt_numero: TextView
    private lateinit var txt_bairro: TextView
    private lateinit var txt_telefone: TextView
    private lateinit var txt_dataNasc: TextView
    private lateinit var txt_cep: TextView
    private lateinit var txt_complemento: TextView
    private lateinit var imagemPerfil : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        btn_editar = button
        btn_voltar = button2

        btn_editar.setOnClickListener { editar() }
        btn_voltar.setOnClickListener { onBackPressed() }

        imagemPerfil = findViewById(R.id.imageView5)

        database = FirebaseDatabase.getInstance().reference

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = user.email
            txt_email = findViewById(R.id.textView)
            txt_email.text = email
            var query: Query = database.child("usuarios").orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //Passar os dados para a interface grafica
                    for (snapshot in dataSnapshot.getChildren()) {
                        val usuario = snapshot.getValue(Usuario::class.java!!)
                        txt_nome = findViewById(R.id.editText1)
                        txt_rua = findViewById(R.id.editText3)
                        txt_numero = findViewById(R.id.editText4)
                        txt_bairro = findViewById(R.id.editText5)
                        txt_telefone = findViewById(R.id.editText6)
                        txt_dataNasc = findViewById(R.id.editText7)
                        txt_cep = findViewById(R.id.editText8)
                        txt_complemento = findViewById(R.id.editText9)

                        txt_nome.text = usuario?.nome
                        txt_rua.text = usuario?.rua
                        txt_numero.text = usuario?.numero
                        txt_bairro.text = usuario?.bairro
                        txt_telefone.text = usuario?.telefone
                        txt_dataNasc.text = usuario?.dataNasc
                        txt_cep.text = usuario?.cep
                        txt_complemento.text = usuario?.complemento

                        Picasso.get()
                            .load(usuario?.foto)
                            .into(imagemPerfil)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Se ocorrer um erro
                }
            })

        }
    }

    fun editar() {

        val pd = ProgressDialog(this@EditarPerfilActivity)
        pd.setMessage("Salvando...")
        pd.show()

        database = FirebaseDatabase.getInstance().reference

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = user.email
            var query: Query = database.child("usuarios").orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //Passar os dados para a interface grafica
                    for (snapshot in dataSnapshot.getChildren()) {
                        val usuario = snapshot.getValue(Usuario::class.java!!)

                        txt_nome = findViewById(R.id.editText1)
                        txt_rua = findViewById(R.id.editText3)
                        txt_numero = findViewById(R.id.editText4)
                        txt_bairro = findViewById(R.id.editText5)
                        txt_telefone = findViewById(R.id.editText6)
                        txt_dataNasc = findViewById(R.id.editText7)
                        txt_cep = findViewById(R.id.editText8)
                        txt_complemento = findViewById(R.id.editText9)

                        usuario?.nome = txt_nome.text.toString()
                        usuario?.rua = txt_rua.text.toString()
                        usuario?.numero = txt_numero.text.toString()
                        usuario?.bairro = txt_bairro.text.toString()
                        usuario?.telefone = txt_telefone.text.toString()
                        usuario?.dataNasc = txt_dataNasc.text.toString()
                        usuario?.cep = txt_cep.text.toString()
                        usuario?.complemento = txt_complemento.text.toString()

                        database.child("usuarios").child(user.uid).child("nome").setValue(usuario?.nome)
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Se ocorrer um erro
                }
            })

        }
    }

}
