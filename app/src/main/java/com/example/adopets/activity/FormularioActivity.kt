package com.example.adopets.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adopets.R
import android.view.View
import android.widget.*
import com.example.adopets.model.Formulario
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import kotlinx.android.synthetic.main.activity_formulario.*


class FormularioActivity : AppCompatActivity() {
    var pgtResidencia: String = ""
    var pgtPessoasMoram: String = ""
    var pgtAnimaisCasa: String = ""
    var preenchido = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)
        init()

        pgtResidencia = ""
        pgtPessoasMoram = ""
        pgtAnimaisCasa = ""

        btn_continuar.setOnClickListener{

            if(pgtResidencia.isNotEmpty()){
                if(pgtPessoasMoram.isNotEmpty()){
                    if(pgtAnimaisCasa.isNotEmpty()){
                        intent = Intent(applicationContext, Formulario2Activity::class.java)
                        intent.putExtra("pgtResidencia", pgtResidencia)
                        intent.putExtra("pgtPessoasMoram",pgtPessoasMoram)
                        intent.putExtra("pgtAnimaisCasa", pgtAnimaisCasa)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Campo vazio!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Campo vazio!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Campo vazio!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun init(){

        val spinner1: Spinner = findViewById(R.id.spinner1)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, R.array.form_moradia, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner1.adapter = adapter
        }
        //spinner1.setOnClickListener()

        val spinner2: Spinner = findViewById(R.id.spinner2)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, R.array.form_pessoas, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter
        }

        val spinner3: Spinner = findViewById(R.id.spinner3)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, com.example.adopets.R.array.form_animais, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner3.adapter = adapter
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
                pgtResidencia = spinner1.getItemAtPosition(position).toString()
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
                pgtPessoasMoram = spinner2.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        spinner3.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtAnimaisCasa = spinner3.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    }
}
