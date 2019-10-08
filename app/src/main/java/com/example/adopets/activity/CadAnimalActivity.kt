package com.example.adopets.activity

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.example.adopets.R
import com.example.adopets.fragment.BottomSheetFotoCadastro
import com.example.adopets.helper.Permissao
import com.example.adopets.model.Animal
import com.example.adopets.model.Usuario
import com.example.adopets.utils.MyMaskEditText
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kofigyan.stateprogressbar.StateProgressBar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_cad_animal.*
import kotlinx.android.synthetic.main.activity_cadastro_etapas.*
import kotlinx.android.synthetic.main.activity_cadastro_etapas.btn_continuar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.btn_voltar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.dataNasc

class CadAnimalActivity : AppCompatActivity(), BottomSheetFotoCadastro.BottomSheetListener {
    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private val permisssaoCamera= arrayOf(Manifest.permission.CAMERA) //array com as permições que o app precisará (camera)
    private val permisssaoGaleria = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) //array com as permições que o app precisará (Galeria)
    private val SELECAO_CAMERA = 100
    private val SELECAO_GALERIA = 200
    private var imagem: Bitmap? = null
    private lateinit var imagemPerfil : CircleImageView
    private lateinit var rdGroup :RadioGroup
    private lateinit var rdSexo :RadioGroup
    private lateinit var database : DatabaseReference
	
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_animal)
        linear1 = findViewById(R.id.step1)
        linear2 = findViewById(R.id.step2)

        database = FirebaseDatabase.getInstance().reference

        imagemPerfil = img_cadastro_animal
        rdGroup = tipo
        rdSexo = sexo


        btn_cadastro_foto_animal.setOnClickListener{
            val opcao = BottomSheetFotoCadastro()
            opcao.show(supportFragmentManager,"bottonSheet")
        }

        data_animal.myCustomMask("##/##/####")

        verificaEtapa()
        btn_finalizar.setOnClickListener{
            situacaoProcessoAnimal()
        }


    }

    override fun onButtonClicked(id: Int) {
        when (id) {
            R.id.btn_galeria_foto -> {
                //abre as permissoes para galeria/ se o usuario tiver permissao irá abrir a galeria
                if (Permissao.validarPermissao(permisssaoGaleria, this, SELECAO_GALERIA)) {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivityForResult(intent, SELECAO_GALERIA)
                    }
                }
            }

            R.id.btn_camera_foto -> {
                //abre as permissoes para camera/ se o usuario tiver permissao irá abrir a camera
                if (Permissao.validarPermissao(permisssaoCamera, this, SELECAO_CAMERA)) {
                    if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivityForResult(intent, SELECAO_CAMERA)
                        }
                    } else {
                        //notFound
                    }

                }


            }
        }
    }

    //metodo de acesso a classe da maskara
    fun EditText.myCustomMask(mask: String) {
        addTextChangedListener(MyMaskEditText(this, mask))}



    //configura as etapas do cadastro
    fun verificaEtapa(){

            btn_continuar.setOnClickListener() {

                //se a barra estiver na etapa 1, tornara a etapa 2 desativada

                if (linear1.visibility == View.VISIBLE) {
                    btn_continuar.visibility = View.VISIBLE
                    if(confirma(1)) {
                        btn_continuar.visibility = View.VISIBLE
                        btn_finalizar.visibility = View.GONE
                        btn_voltar.visibility = View.VISIBLE


              //progresso = 2
                        linear1.visibility = View.GONE
                        btn_continuar.visibility = View.GONE
                        btn_finalizar.visibility = View.VISIBLE
                        linear2.visibility = View.VISIBLE
                    }

                } else if (linear2.visibility == View.VISIBLE) {


                    if(confirma(2)){
                       btn_finalizar.setOnClickListener{
                           situacaoProcessoAnimal()

                       }
                      //  startActivity(Intent(this, PerfilAnimalActivity::class.java))
                    }
                }
            }

            btn_voltar.setOnClickListener() {

//progresso = 1
                linear1.visibility = View.VISIBLE
                linear2.visibility = View.GONE

                btn_voltar.visibility = View.GONE
            }

    }

    //verifica campos conforme a etapa
    fun confirma(etapa: Int): Boolean{

        var nom = nome.text.toString()
        var rac = raca.text.toString()
        var tam = tamanho.text.toString()
        val id = rdGroup.checkedRadioButtonId
        val selectedButton = findViewById<RadioButton>(id)
        //Seta o tipo ao Objeto
        var tipo = selectedButton.text.toString()


        if(etapa == 1){

            if (nom.isEmpty()) {
                nome.error = "Campo obrigatório!"
                return false
            }

            if (tam.isEmpty()) {
                tamanho.error = "Campo obrigatório!"
                return false
            }

            //pre visu de animal na etapa 2
            nome2.text = nom
        } else if(etapa == 2){
            //restantes inputs
            var neces = necessidade.text.toString()
            var desc = descricao.text.toString()
            var dataN =data_animal.text.toString()

            val selectedButtonSexo = findViewById<RadioButton>(rdSexo.checkedRadioButtonId)
            //Seta o sexo ao Objeto
            var sexo = selectedButtonSexo.text.toString()

            val animal = Animal()
            animal.nome = nom
            animal.sexo = sexo
            animal.raca = rac
            animal.descricao = desc
            animal.tamanho = tam
            animal.necessidade = neces
            animal.tipo = tipo
            animal.dataNasc = dataN

            val usuarios = database.child("animal")
            val ref = usuarios.push()
            ref.setValue(animal)

            /*

            if (tip.isEmpty()) {
                tipo.error = "Campo obrigatório!"
                return false
            }*/
        }

        return true
    }

    //na tabela processo
    fun situacaoProcessoAnimal(){
        val builder = android.support.v7.app.AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Este animal precisa...")
      //  builder.setMessage("Para a melhor utilização do app é necessário aceitar as permissões")
        builder.setCancelable(false)
      //  builder.setNegativeButton("Receber Ajuda")
        builder.setPositiveButton("Ser Doado" ){ dialogInterface, i ->
            dialogMotivoDoacao()
        }



        val dialog = builder.create()
        dialog.show()
        //motivo
        //se quer doaç respos e explica
    }

    fun dialogMotivoDoacao(){

        val builder = android.support.v7.app.AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Por que você quer doá-lo?")
        //TextInput com id motivo para adicionar na tabela doacao


       // builder.setMessage("Para a melhor utilização do app é necessário aceitar as permissões")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar" ){ dialogInterface, i ->
            escolhaAdocaoResponsavel()
        }



        val dialog = builder.create()
        dialog.show()
    }

    fun escolhaAdocaoResponsavel(){
        val builder = android.support.v7.app.AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Você quer realizar uma doação responsável?")


        builder.setMessage("É uma forma de feedback para você receber formulários de respostas dos candidatos e escolher o adotante ideal para o seu pet!")
        builder.setCancelable(false)
        //salvar escolha do usuário
        builder.setPositiveButton("Sim" ){ dialogInterface, i ->  }




        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao()

            }else if (requestCode == SELECAO_GALERIA) { //se foi aceita a permissao ira abrir a opcao da camera ou galeria
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA)
                }

            } else if (requestCode == SELECAO_CAMERA) {
                if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivityForResult(intent, SELECAO_CAMERA)
                    }
                } else {
                    //notFound
                }
            }

        }
    }

    fun alertaPermissao(){
        val builder = AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Permissões Negadas")
        builder.setMessage("Para a melhor utilização do app é necessário aceitar as permissões")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar" ){ dialogInterface, i ->  }



        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == RESULT_OK) {
            try {
                when (requestCode) {
                    SELECAO_CAMERA -> {
                        imagem = data?.extras?.get("data") as Bitmap
                    }
                    SELECAO_GALERIA -> {
                        val localImagemSelecionada = data?.data
                        imagem = MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            localImagemSelecionada
                        )

                    }
                }
                if(imagem != null){
                    imagemPerfil.setImageBitmap(imagem)
                }
            } catch (e:java.lang.Exception) {
                e.printStackTrace()
            }
        }

    }
}






