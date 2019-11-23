package com.example.adopets.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.adopets.R
import com.example.adopets.model.Servico
import com.example.adopets.utils.MyMaskEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_cad_servico.*
import java.util.*


//MER: tipo, descricao, dataInicio, dataFim, // codigo gerado

class CadServicoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth

    var pgtServico: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_servico)
        dataInicio.myCustomMask("##/##/####")
        dataFim.myCustomMask("##/##/####")
        init()

        database = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()


        btn_finalizar.setOnClickListener{
            cadastrarServico()
        }

    }

    fun cadastrarServico() {
        var dataI = dataInicio.text.toString()
        var dataF = dataFim.text.toString()
        var desc = descricao.text.toString()
        
        

        if (dataI.isEmpty()) {
            dataInicio.error = "É necessário a data de início!"
            Toast.makeText(this, dataInicio.error, Toast.LENGTH_SHORT)
                .show()
        }
        if (dataF.isEmpty()) {
            dataFim.error = "É necessário a data de término!"
            Toast.makeText(this, dataFim.error, Toast.LENGTH_SHORT)
                .show()
        }
        if (desc.isEmpty()) {
            descricao.error = "É necessário descrever sobre a atividade!"
            Toast.makeText(this, descricao.error, Toast.LENGTH_SHORT)
                .show()
        }
        //BANCO
        var id = gerarId()
        val servico = Servico()
        servico.id = id
        servico.dataInicio = dataI
        servico.dataFim = dataF
        servico.descricao = desc
        servico.tipo = pgtServico

        val servicos = database.child("servico")

        val ref = servicos.child(id)
        servico.id = id
        ref.setValue(servico)

        val pd = ProgressDialog(this)
        pd.setMessage("Cadastrando voluntariado...")
        pd.show()
        startActivity(Intent(applicationContext, HomeActivity::class.java))

    }

    fun gerarId(): String {
        // Determia as letras que poderão estar presente nas chaves
        val letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ"
        var random = Random()
        var armazenaChaves = ""
        var index = -1
        for (i in 0..9) {
            index = random.nextInt(letras.length)
            armazenaChaves += letras.substring(index, index + 1)
        }
        return armazenaChaves
    }

    fun init(){
        val spinnerServico: Spinner = findViewById(R.id.spinnerServico)
        ArrayAdapter.createFromResource(
            this, R.array.servico, android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerServico.adapter = adapter
        }

        spinnerServico.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtServico = spinnerServico.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

    }
    fun EditText.myCustomMask(mask: String) {
        addTextChangedListener(MyMaskEditText(this, mask))
    }
}
