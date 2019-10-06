package com.example.adopets.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.adopets.R
import com.example.adopets.activity.CadastroActivity
import com.example.adopets.activity.EditarPerfilActivity
import com.example.adopets.activity.MainActivity
import com.example.adopets.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.fragment_perfil.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PerfilFragment : Fragment() {

    private lateinit var btn_editar: Button
    private lateinit var btn_apagar: Button

    private lateinit var database: DatabaseReference
    private lateinit var txt_nome: TextView
    private lateinit var txt_email: TextView
    private lateinit var txt_endereco: TextView
    private lateinit var txt_telefone: TextView
    private lateinit var txt_dataNasc: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_perfil, container, false)

        database = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser

        btn_editar = view.findViewById(R.id.button)
        btn_apagar = view.findViewById(R.id.button2)

        btn_editar.setOnClickListener {
            val intent = Intent(activity, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        btn_apagar.setOnClickListener {

            user?.let {
                val email = user.email
                txt_email = view.findViewById(R.id.textView8)
                txt_email.text = email
                var query: Query = database.child("usuarios").orderByChild("email").equalTo(email)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        //Passar os dados para a interface grafica
                        for (snapshot in dataSnapshot.getChildren()) {
                            snapshot.ref.removeValue()
                            user.delete()
                            startActivity(Intent(activity, MainActivity::class.java))
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        //Se ocorrer um erro
                    }
                })

            }

        }

        user?.let {
            val email = user.email
            txt_email = view.findViewById(R.id.textView8)
            txt_email.text = email
            var query: Query = database.child("usuarios").orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //Passar os dados para a interface grafica
                    for (snapshot in dataSnapshot.getChildren()) {
                        val usuario = snapshot.getValue(Usuario::class.java!!)
                        txt_nome = view.findViewById(R.id.textView7)
                        txt_endereco = view.findViewById(R.id.textView9)
                        txt_telefone = view.findViewById(R.id.textView10)
                        txt_dataNasc = view.findViewById(R.id.textView11)

                        txt_nome.text = usuario?.nome
                        txt_endereco.text =
                            usuario?.rua + ", " + usuario?.numero + ", " + usuario?.bairro
                        txt_telefone.text = usuario?.telefone
                        txt_dataNasc.text = usuario?.dataNasc
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Se ocorrer um erro
                }
            })

        }

        // Inflate the layout for this fragment
        return view
    }

}
