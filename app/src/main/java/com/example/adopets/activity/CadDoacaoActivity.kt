package com.example.adopets.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.model.Doador
import com.example.adopets.model.Processo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_cad_animal.*
import kotlinx.android.synthetic.main.activity_cad_doacao.*
import java.text.SimpleDateFormat
import java.util.*


//MER: codigo do processo e id do doador, salvar dataCriacao (data atual do celular), motivo da doacao e situacao (EDR - em doacao responsavel ou ED - em doacao)
//


//Cadastro Doacao
class CadDoacaoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_doacao)


        database = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        btn_finalizar.setOnClickListener{
            cadastrarDoacao()
        }
    }
    fun cadastrarDoacao(){

        var mot = motivo.text.toString()
        val idR = situacao.checkedRadioButtonId
        val selectedButton = findViewById<RadioButton>(idR)
        //Seta o tipo ao Objeto
        var situDoaResp = selectedButton.text.toString()

        if (mot.isEmpty()) {
            motivo.error = "É necessário o motivo da doação!"
            Toast.makeText(this, motivo.error, Toast.LENGTH_SHORT)
                .show()


        }


        //BANCO
        var id = gerarId()
        val processo = Processo()
        processo.id = id
        processo.motivo = mot
        processo.situacao = situDoaResp
        database = FirebaseDatabase.getInstance().reference
        var query =
            database.child("usuarios").orderByChild("id").equalTo(auth!!.currentUser!!.uid)
        processo.doador = query as Doador
        processo.dataCriacao = dataAtual()

        val processos = database.child("processo")

        val ref = processos.child(id)
        processo.id = id
        ref.setValue(processo)

        val pd = ProgressDialog(this)
        pd.setMessage("Cadastrando doação...")
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

    fun dataAtual(): String{
        var formataData = SimpleDateFormat("dd-MM-yyyy");
        var data = Date();
        var dataFormatada = formataData.format(data);
        return dataFormatada
    }
}
