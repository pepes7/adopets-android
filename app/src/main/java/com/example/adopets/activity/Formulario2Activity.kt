package com.example.adopets.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.adopets.R
import android.view.View
import android.widget.*
import com.example.adopets.model.Formulario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_formulario2.*


class Formulario2Activity : AppCompatActivity(){
    var pgtOndeTempo: String = ""
    var pgtQuantoTempo: String = ""
    private lateinit var dados: Bundle
    private val formulario:Formulario = Formulario()
    private lateinit var database : DatabaseReference
    private lateinit var radioGroup: RadioGroup
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario2)
        dados = intent.extras
        auth = FirebaseAuth.getInstance()
        formulario.pgtResidencia = dados.getString("pgtResidencia")
        formulario.pgtPessoasMoram = dados.getString("pgtPessoasMoram")
        formulario.pgtAnimaisCasa = dados.getString("pgtAnimaisCasa")
        database = FirebaseDatabase.getInstance().reference
        init()

        pgtOndeTempo = ""
        pgtQuantoTempo =  ""

        btn_enviar.setOnClickListener{
            val selectedButton = findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
            formulario.pgtProtegerFamilia = selectedButton.text.toString()

            //pega o id do animal
            formulario.idAnimal =  dados.getString("id")

            formulario.idAdotante = auth!!.currentUser!!.uid

            if(pgtOndeTempo.isNotEmpty()){
                if(pgtQuantoTempo.isNotEmpty()){
                    val form = database.child("formulario")
                    form.push().setValue(formulario)

                    Toast.makeText(this, "Sua resposta foi enviada!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Campo vazio 1!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Campo vazio 2!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun init(){
        radioGroup = radioOpcao

        val spinner1: Spinner = findViewById(R.id.spinner1)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, R.array.form_foraDeCasa, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner1.adapter = adapter
        }


        val spinner2: Spinner = findViewById(R.id.spinner2)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, com.example.adopets.R.array.form_tempoForaDeCasa, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter
        }

        spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtOndeTempo = spinner1.getItemAtPosition(position).toString()
                formulario.pgtOndeTempo = pgtOndeTempo

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })

        spinner2.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtQuantoTempo = spinner2.getItemAtPosition(position).toString()
                formulario.pgtQuantoTempo = pgtQuantoTempo
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })
    }
}
