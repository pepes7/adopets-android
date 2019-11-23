
package com.example.adopets.activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.example.adopets.R
import com.example.adopets.fragment.BottomSheetFotoCadastro
import com.example.adopets.helper.Permissao
import com.example.adopets.model.Animal
import com.example.adopets.utils.MyMaskEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_cad_animal.*
import kotlinx.android.synthetic.main.activity_cad_animal.descricao
import kotlinx.android.synthetic.main.activity_cad_animal.imageProfile2
import kotlinx.android.synthetic.main.activity_cad_animal.necessidade
import kotlinx.android.synthetic.main.activity_cad_animal.nome
import kotlinx.android.synthetic.main.activity_cad_animal.nome2
import kotlinx.android.synthetic.main.activity_cad_animal.sexo
import kotlinx.android.synthetic.main.activity_cad_animal.tipo
import kotlinx.android.synthetic.main.activity_cadastro_etapas.*
import kotlinx.android.synthetic.main.activity_cadastro_etapas.btn_continuar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.btn_voltar
import kotlinx.android.synthetic.main.activity_cadastro_etapas.dataNasc
import java.io.ByteArrayOutputStream
import java.util.*


class CadAnimalActivity : AppCompatActivity(), BottomSheetFotoCadastro.BottomSheetListener {
    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private val permisssaoCamera =
        arrayOf(Manifest.permission.CAMERA) //array com as permições que o app precisará (camera)
    private val permisssaoGaleria =
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) //array com as permições que o app precisará (Galeria)
    private val SELECAO_CAMERA = 100
    private val SELECAO_GALERIA = 200
    private var imagem: Bitmap? = null
    private lateinit var imagemPerfil: CircleImageView
    private lateinit var rdGroup: RadioGroup
    private lateinit var rdSexo: RadioGroup
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var bairroUserReference: DatabaseReference
    private lateinit var bairroUser: String

    var pgtNecessidade: String = ""
    var pgtRaca: String = ""
    var pgtTamanho: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cad_animal)
        linear1 = findViewById(R.id.step1)
        linear2 = findViewById(R.id.step2)

        inicializar()

        database = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        auth = FirebaseAuth.getInstance()


        //recupera o bairro do usuario
        bairroUserReference = FirebaseDatabase.getInstance().reference.child("usuarios")
            .child(auth!!.currentUser!!.uid).child("bairro")
        recuperarBairro()

        imagemPerfil = img_cadastro_animal
        rdGroup = tipo
        rdSexo = sexo


        btn_cadastro_foto_animal.setOnClickListener {
            val opcao = BottomSheetFotoCadastro()
            opcao.show(supportFragmentManager, "bottonSheet")
        }

        data_animal.myCustomMask("##/##/####")

        verificaEtapa()

        btn_servico.setOnClickListener {
            confirma(2, "Aguardando ajuda voluntária")

        }

        btn_doacao.setOnClickListener{
            confirma(2, "Aguardando adoção")
        }
    }

    fun recuperarBairro() {
        bairroUserReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //recupera o bairro do usuario
                bairroUser = dataSnapshot.getValue().toString()

            }

        })
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
        addTextChangedListener(MyMaskEditText(this, mask))
    }


    //configura as etapas do cadastro
    fun verificaEtapa() {

        btn_continuar.setOnClickListener() {

            //se a barra estiver na etapa 1, tornara a etapa 2 desativada

            if (linear1.visibility == View.VISIBLE) {
                btn_continuar.visibility = View.VISIBLE

                if (confirma(1, "")) {
                    btn_continuar.visibility = View.VISIBLE

                    btn_voltar.visibility = View.VISIBLE


                    //progresso = 2
                    linear1.visibility = View.GONE
                    btn_continuar.visibility = View.GONE

                    linear2.visibility = View.VISIBLE
                }

            } else if (linear2.visibility == View.VISIBLE) {


            }
        }

        btn_voltar.setOnClickListener() {

            //progresso = 1
            linear1.visibility = View.VISIBLE
            linear2.visibility = View.GONE

            btn_voltar.visibility = View.GONE
            btn_continuar.visibility = View.VISIBLE

        }

    }

    //verifica campos conforme a etapa
    fun confirma(etapa: Int, situacao: String): Boolean {

        var camposVazios: String

        var nom = nome.text.toString()
        val id = rdGroup.checkedRadioButtonId
        val selectedButton = findViewById<RadioButton>(id)
        //Seta o tipo ao Objeto
        var tipo = selectedButton.text.toString()

        camposVazios = ""

        if (etapa == 1) {

            if (nom.isEmpty()) {
                //   nome.error = "Campo obrigatório!"
                camposVazios += "Nome; "
                //    return false
            }

            /* if (tam.isEmpty()) {
                tamanho.error = "Campo obrigatório!"
                return false
            }*/

            //pre visu de animal na etapa 2
            nome2.text = nom
            if (imagem != null) {
                imageProfile2.setImageBitmap(imagem)
            } else { //caso a imagem seja nula retorna false
                //alertaImagem("Adicione uma foto do animal")
                camposVazios += "Imagem; "
                // return false
            }
            if (pgtRaca.isEmpty()) {
                camposVazios += "Raça; "
                //   alertaImagem("Preencha a raca")

            }

            if (pgtTamanho.isEmpty()) {
                camposVazios += "Porte."
            }

            if (camposVazios.equals("")) {
                return true
            } else {
                alertaCamposVazios(camposVazios)
                return false
            }

        } else if (etapa == 2) {
            val pd = ProgressDialog(this)
            pd.setMessage("Cadastrando animal...")
            pd.show()
            //restantes inputs
            var neces = pgtNecessidade
            var desc = descricao.text.toString()
            var dataN = data_animal.text.toString()

            val selectedButtonSexo = findViewById<RadioButton>(rdSexo.checkedRadioButtonId)
            //Seta o sexo ao Objeto
            var sexo = selectedButtonSexo.text.toString()

            val animal = Animal()
            animal.nome = nom
            animal.sexo = sexo
            animal.tipo = tipo
            animal.raca = pgtRaca

            if (animal.sexo.equals("Fêmea") && tipo.equals("Gato")) {
                animal.tipo = "Gata"

            } else if (animal.sexo.equals("Fêmea") && tipo.equals("Cachorro")) {
                animal.tipo = "Cadela"
            } else if (animal.sexo.equals("Macho") && tipo.equals("Cachorro")) {
                animal.tipo = "Cachorro"
            } else animal.tipo = "Gato"

            animal.descricao = desc
            animal.necessidade = neces

            animal.dataNasc = dataN
            animal.doador = auth!!.currentUser!!.uid
            animal.tamanho = pgtTamanho
            animal.situacao = situacao
            animal.bairro = bairroUser


            val animais = database.child("animal")
            var id = gerarId()
            val ref = animais.child(id)
            animal.id = id
            ref.setValue(animal)
            if (imagem != null) {
                salvarFoto(ref, animal, id, situacao)
            } else {
                Toast.makeText(this, "Animal não foi cadastrado :(", Toast.LENGTH_SHORT).show()
            }


        }
        
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao()

            } else if (requestCode == SELECAO_GALERIA) { //se foi aceita a permissao ira abrir a opcao da camera ou galeria
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

    fun alertaPermissao() {
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Permissões Negadas")
        builder.setMessage("Para a melhor utilização do app é necessário aceitar as permissões")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar") { dialogInterface, i -> }


        val dialog = builder.create()
        dialog.show()
    }


    /*   fun alertaImagem(text : String) {
           val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
           builder.setTitle("Foto nula")
           builder.setMessage(text)
           builder.setCancelable(false)
           builder.setPositiveButton("Confirmar") { dialogInterface, i -> }


    fun alertaCamposVazios(campos: String) {
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Ops...")
        builder.setMessage("Preencha os campos obrigatórios: /n" + campos)
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar") { dialogInterface, i -> }

           val dialog = builder.create()
           dialog.show()
       }
   */

    fun alertaCamposVazios(campos: String) {
        val builder = AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog)
        builder.setTitle("Ops...")
        builder.setMessage("Todos estes campos são obrigatórios, você tem que preencher: " + campos)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok, irei preencher") { dialogInterface, i -> }


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
                if (imagem != null) {
                    imagemPerfil.setImageBitmap(imagem)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

    }

    fun salvarFoto(ref: DatabaseReference, u: Animal, id: String, proximaTela: String) {
        //recuperar dados da imagem para o firebase
        val baos = ByteArrayOutputStream()

        imagem?.compress(Bitmap.CompressFormat.JPEG, 70, baos)

        val dadosImagem = baos.toByteArray()

        //Salvar no Firebase
        val imagemRef = storageReference
            .child("imagens")
            .child("animal")
            .child(id)
            .child("perfil.jpeg")

        val uploadTask = imagemRef.putBytes(dadosImagem)
        uploadTask.addOnFailureListener {
            //Se houve erro no upload da imageFile
            Toast.makeText(this, "Erro ao salvar  a foto", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            imagemRef.downloadUrl.addOnSuccessListener {
                u.foto = it.toString()
                ref.child("foto").setValue(u.foto)
                //Se o upload da imageFile foi realizado com sucesso
                /*   Toast.makeText(this, "Animal cadastrado com sucesso!", Toast.LENGTH_SHORT)
                       .show()*/
                // startActivity(Intent(applicationContext, HomeActivity::class.java))
                //  startActivity(Intent(applicationContext, CadDoacaoActivity::class.java))
                if (proximaTela.equals("Aguardando adoção")) {
                    startActivity(Intent(baseContext, CadDoacaoActivity::class.java))
                } else {
                    startActivity(Intent(baseContext, CadServicoActivity::class.java))
                }
            }

        }
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

    fun inicializar() {

        val spinnerTamanho: Spinner = findViewById(R.id.spinnerTamanho)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, R.array.porte, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerTamanho.adapter = adapter
        }

        val spinnerNecessidade: Spinner = findViewById(R.id.spinnerNecessidade)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this, R.array.necessidade, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerNecessidade.adapter = adapter
        }

        //val selectedButtonTipo = findViewById<RadioButton>(tipo.checkedRadioButtonId)

        // val tipoOpcao = selectedButtonTipo.text.toString()


        val spinnerRaca: Spinner = findViewById(R.id.spinnerServico)
        ArrayAdapter.createFromResource(
            this, R.array.racas_cachorro, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerRaca.adapter = adapter
        }



        tipo.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            var id: Int = tipo.checkedRadioButtonId
            if (id != -1) { // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio: RadioButton = findViewById(id)
                /*  Toast.makeText(
                      applicationContext, "On button click : ${radio.text}",
                      Toast.LENGTH_SHORT
                  ).show()*/


                if (radio.text.equals("Cachorro")) {
                    ArrayAdapter.createFromResource(
                        this, R.array.racas_cachorro, android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        spinnerRaca.adapter = adapter
                    }
                } else if (radio.text.equals("Gato")) {
                    ArrayAdapter.createFromResource(
                        this, R.array.racas_gato, android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        spinnerRaca.adapter = adapter
                    }
                }
            }
        })



        spinnerNecessidade.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtNecessidade = spinnerNecessidade.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        spinnerRaca.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtRaca = spinnerRaca.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        spinnerTamanho.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtTamanho = spinnerTamanho.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })



    }
}