package com.example.adopets.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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
    private lateinit var situDr: LinearLayout
    private lateinit var situD: LinearLayout
    private lateinit var btn_finalizar: Button
    private lateinit var dr: ImageView
    private lateinit var d: ImageView
    var situDoacao = ""
    private lateinit var inform: TextView
    private lateinit var inform2: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_doacao)

        inform = findViewById(R.id.informacao)
        inform2 = findViewById(R.id.informacao2)
        situD = findViewById(R.id.doac)
        situDr = findViewById(R.id.doacR)
        dr = findViewById(R.id.dr)
        d = findViewById(R.id.d)

        btn_finalizar = findViewById(R.id.btn_finalizar)

        btn_finalizar.setOnClickListener{
            cadastrarDoacao()
        }

        dr.setOnClickListener {
            situDoacao = "Doação responsável"
            inform.visibility = View.VISIBLE
            inform2.visibility = View.GONE
            situD.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background)
            situDr.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background)
        }

        d.setOnClickListener {

            situDoacao = "Doação comum"
            inform2.visibility = View.VISIBLE
            inform.visibility = View.GONE
            situDr.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background)
            situD.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background)

        }

        database = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()

        btn_finalizar.setOnClickListener{
            cadastrarDoacao()
        }
    }
    fun cadastrarDoacao(){

        var mot = motivo.text.toString()
        //val idR = situacao.checkedRadioButtonId
        //val selectedButton = findViewById<RadioButton>(idR)
        //Seta o tipo ao Objeto
        //var situDoaResp = selectedButton.text.toString()

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
        processo.situacao = situDoacao
        processo.doador = auth!!.currentUser!!.uid
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
