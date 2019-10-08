package com.example.adopets.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.adopets.R
import com.example.adopets.activity.CadAnimalActivity
import com.example.adopets.adapter.AnimalAdapter
import com.example.adopets.model.Animal

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PetsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAnimais: RecyclerView
    private lateinit var  btn_animal: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_pets, container, false)

        btn_animal = view.findViewById(R.id.add)

        btn_animal.setOnClickListener {
            startActivity(Intent(context, CadAnimalActivity::class.java))
        }

        recyclerView = view.findViewById(R.id.recyclerViewAnimais) as RecyclerView
        recyclerViewAnimais = view.findViewById(R.id.recyclerViewAnimais) as RecyclerView

        recyclerView = recyclerViewAnimais
        recyclerView.adapter = AnimalAdapter(animais(), activity!!)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        return view
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
            Animal("Ferrer", "Macho", "Alvorada"))
    }

}
