package com.example.adopets.activity

import android.Manifest
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
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.adopets.fragment.BottomSheetFotoCadastro
import com.example.adopets.helper.Permissao
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class EditarPerfilActivity : AppCompatActivity() , BottomSheetFotoCadastro.BottomSheetListener {
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
    private val permisssaoCamera= arrayOf(Manifest.permission.CAMERA) //array com as permições que o app precisará (camera)
    private val permisssaoGaleria = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) //array com as permições que o app precisará (Galeria)
    private val SELECAO_CAMERA = 100
    private val SELECAO_GALERIA = 200
    private var imagem: Bitmap? = null
    private lateinit var imagemPerfil : CircleImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference : StorageReference
    private lateinit var btnInserirFoto : Button
    private lateinit var btnExcluirFoto : Button

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        btn_editar = button
        btn_voltar = button2
        btnInserirFoto = btn_inserir_foto
        btnExcluirFoto = btn_excluir_foto

        btnInserirFoto.setOnClickListener{
            val opcao = BottomSheetFotoCadastro()
            opcao.show(supportFragmentManager,"bottonSheet")
        }

        btn_editar.setOnClickListener { editar() }
        btn_voltar.setOnClickListener { onBackPressed() }

        imagemPerfil = findViewById(R.id.imageView5)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

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

                        database.child("usuarios").child(user.uid).setValue(usuario)
                        if (imagem!=null){
                            salvarFoto(database.child("usuarios").child(user.uid))
                        }else{
                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                        }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //Se ocorrer um erro
                }
            })

        }
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
                    //recuperar dados da imagem para o firebase
                    imagemPerfil.setImageBitmap(imagem)
                }
            } catch (e:java.lang.Exception) {
                e.printStackTrace()
            }
        }

    }

    fun salvarFoto(ref:DatabaseReference){
        //recuperar dados da imagem para o firebase
        val baos =  ByteArrayOutputStream()

        imagem?.compress(Bitmap.CompressFormat.JPEG, 70, baos)

        val dadosImagem = baos.toByteArray()

        //Salvar no Firebase
        val imagemRef = storageReference
            .child("imagens")
            .child("perfil")
            .child(auth!!.currentUser?.uid.toString())
            .child("perfil.jpeg")

        val uploadTask = imagemRef.putBytes(dadosImagem)
        uploadTask.addOnFailureListener{
            //Se houve erro no upload da imageFile
            Toast.makeText(this, "Erro ao salvar  a foto", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            imagemRef.downloadUrl.addOnSuccessListener {
                ref.child("foto").setValue(it.toString())
            }
            //Se o upload da imageFile foi realizado com sucesso
            Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }
    }



}
