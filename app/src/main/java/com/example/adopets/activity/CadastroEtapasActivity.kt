package com.example.adopets.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.example.adopets.R
import com.example.adopets.fragment.BottomSheetFotoCadastro
import com.example.adopets.helper.Permissao
import com.example.adopets.model.Usuario
import com.example.adopets.utils.MyMaskEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kofigyan.stateprogressbar.StateProgressBar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_cadastro_etapas.*
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.CEP
import thiagocury.eti.br.exconsumindoviacepkotlinretrofit2.RetrofitInitializer
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat


class CadastroEtapasActivity : AppCompatActivity(), BottomSheetFotoCadastro.BottomSheetListener {
    private lateinit var stateProgressBar: StateProgressBar
    private lateinit var btn_continuar: Button
    private lateinit var btn_voltar: Button
    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private lateinit var email: String
    private lateinit var nome: String
    private lateinit var senha: String
    private lateinit var dados: Bundle
    private lateinit var database : DatabaseReference
    private lateinit var storageReference : StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonAdicionarFoto: Button
    private val permisssaoCamera= arrayOf(Manifest.permission.CAMERA) //array com as permições que o app precisará (camera)
    private val permisssaoGaleria = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) //array com as permições que o app precisará (Galeria)
    private val SELECAO_CAMERA = 100
    private val SELECAO_GALERIA = 200
    private var imagem: Bitmap? = null
    private lateinit var imagemPerfil : CircleImageView

    //metodo da classe BottomSheetFotoCadastro para verificar qual botão foi pressionado
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
    fun inicializaComponentes(){
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        stateProgressBar = findViewById(R.id.progresso)
        btn_continuar = findViewById(R.id.btn_continuar)
        btn_voltar = findViewById(R.id.btn_voltar)
        linear1 = findViewById(R.id.step1)
        linear2 = findViewById(R.id.step2)
        buttonAdicionarFoto = btn_inserir_foto
        imagemPerfil = imageProfile



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_etapas)

        dados = intent.extras
        email = dados.getString("email")
        nome = dados.getString("nome")
        senha = dados.getString("senha")


        inicializaComponentes()

        //coloca a mascara da data no editText
        dataNasc.myCustomMask("##/##/####")
        cep.myCustomMask("#####-###")
        //69020-120

        verificaEtapa()

        btn_inserir_foto.setOnClickListener{
            val opcao = BottomSheetFotoCadastro()
            opcao.show(supportFragmentManager,"bottonSheet")
        }

        //cep
        pesquisar.setOnClickListener {
            if (cep.text.toString().length == 9) {
                searchCEP()
            } else {
                Toast.makeText(this, "CEP inválido!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //configura as etapas do cadastro
    fun verificaEtapa(){
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

        //verifica se a barra de progresso esta ativa
        if (progresso != null) {

            btn_continuar.setOnClickListener() {

                //se a barra estiver na etapa 1, tornara a etapa 2 desativada

                if (stateProgressBar.currentStateNumber == 1) {

                    if(confirma(1)) {
                        btn_voltar.visibility = View.VISIBLE

                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)

                        linear1.visibility = View.GONE
                        linear2.visibility = View.VISIBLE
                    }

                } else if (progresso.currentStateNumber == 2) {
                    confirma(2)
                }
            }

            btn_voltar.setOnClickListener() {

                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

                linear1.visibility = View.VISIBLE
                linear2.visibility = View.GONE

                btn_voltar.visibility = View.GONE
            }

        }


    }

    //verifica campos conforme a etapa
    fun confirma(etapa: Int): Boolean{

        var tel = telefone.text.toString()
        var dataN = dataNasc.text.toString()

        if(etapa == 1){

            if (tel.isEmpty()) {
                telefone.error = "Campo obrigatório!"
                return false
            }

            if (dataN.isEmpty()) {
                dataNasc.error = "Campo obrigatório!"
                return false
            }

        } else if(etapa == 2){
            var comp = complemento.text.toString()
            var ruaR = rua.text.toString()
            var bairroR = bairro.text.toString()
            var cepR = cep.text.toString()
            var numeroR = numero.text.toString()

            if (comp.isEmpty()) {
                complemento.error = "Campo obrigatório!"
                return false
            } else if (ruaR.isEmpty()) {
                rua.error = "Campo obrigatório!"
                return false
            } else if (cepR.isEmpty()) {
                cep.error = "Campo obrigatório!"
                return false
            } else if(bairroR.isEmpty()) {
                bairro.error = "Campo Obrigatório"
                return false
            } else if(numeroR.isEmpty()){
                numero.error = "Campo Obrigatório"
                return false
            } else {
                //firebase
                val u = Usuario()
                u.email = email
                u.nome = nome
                u.senha = senha
                u.dataNasc = dataN
                u.telefone = tel
                u.bairro = bairroR
                u.rua = ruaR
                u.numero = numeroR
                u.complemento = comp
                u.cep = cepR

                val pd = ProgressDialog(this@CadastroEtapasActivity)
                pd.setMessage("Cadastrando...")
                pd.show()

                auth.createUserWithEmailAndPassword(u.email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val usuarios = database.child("usuarios")
                            val ref = usuarios.child(auth!!.currentUser!!.uid)
                            ref.child("id").setValue(auth!!.currentUser?.uid)
                            ref.child("email").setValue(u.email)
                            ref.child("nome").setValue(u.nome)
                            ref.child("dataNasc").setValue(u.dataNasc)
                            ref.child("telefone").setValue(u.telefone)
                            ref.child("bairro").setValue(u.bairro)
                            ref.child("rua").setValue(u.rua)
                            ref.child("numero").setValue(u.numero)
                            ref.child("complemento").setValue(u.complemento)
                            ref.child("cep").setValue(u.cep)

                            if (imagem!=null){
                                salvarFoto(ref,u)
                            }else{
                                Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                            }
                        } else {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                Toast.makeText(
                                    this@CadastroEtapasActivity,
                                    "Senha fraca!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    this@CadastroEtapasActivity,
                                    "Email inválido!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: FirebaseAuthUserCollisionException) {
                                Toast.makeText(
                                    this@CadastroEtapasActivity,
                                    "Usuário já cadastrado!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@CadastroEtapasActivity,
                                    "" + e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

            }
        }

        return true
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

    //metodo de acesso a classe da maskara
    fun EditText.myCustomMask(mask: String) {
        addTextChangedListener(MyMaskEditText(this, mask))}

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
    fun salvarFoto(ref:DatabaseReference,u:Usuario){
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
                    u.foto = it.toString()
                    ref.child("foto").setValue(u.foto)

                }
                //Se o upload da imageFile foi realizado com sucesso
                Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
    }

    fun searchCEP() {
        val cep = findViewById(R.id.cep) as EditText
        val progressDialog = ProgressDialog(this@CadastroEtapasActivity)
        progressDialog.setTitle("Buscando CEP ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val call =
            RetrofitInitializer().apiRetrofitService().getEnderecoByCEP(cep.text.toString())

        call.enqueue(object : retrofit2.Callback<CEP> {

            override fun onResponse(call: retrofit2.Call<CEP>, response: retrofit2.Response<CEP>) {
                response.let {

                    val CEPs: CEP? = it.body()

                    val address = findViewById<TextView>(R.id.rua)
                    address.text = CEPs!!.logradouro.toString()
                    val burgh = findViewById<TextView>(R.id.bairro)
                    burgh.text = CEPs.bairro.toString()

                    progressDialog.dismiss()

                    Log.i("CEP", CEPs.toString())
                }
            }

            override fun onFailure(call: retrofit2.Call<CEP>, t: Throwable) {
                progressDialog.dismiss()
                println("Erro $ t.message")
            }
        })
    }

}



//   val visibility = if (linear2.visibility == View.VISIBLE) View.GONE else View.VISIBLE


/*
public class PerfilActivity extends AppCompatActivity {

    private FirebaseAuth auth =  FirebaseConfig.getFirebaseAuth();
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private EditText nome;
    private EditText email;
    private RadioButton rbMasc;
    private RadioButton rbFemin;
    private TextView textView;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView imagemPerfil;
    private StorageReference storageReference;
    private Usuario user;
    private  Bitmap imagem = null;
    private DatabaseReference databaseReference = FirebaseConfig.getFirebaseDatabase().child("usuarios");
    private ProgressDialog dialogData;
    private ProgressBar progressImage;



    private String[] permisssoes = new String[]{
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
        if(permissaoResultado == PackageManager.PERMISSION_DENIED){
            alertaValidaPermissao();
        }
    }
    }

    public void alertaValidaPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para a melhor utilização do app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Permissao.validarPermissao(permisssoes,this, 1);

        //firebase storage
        storageReference = FirebaseConfig.getFirebaseStorage();

        user = new Usuario();
        imagemPerfil = findViewById(R.id.imageProfile);
        progressImage = findViewById(R.id.progressImage);

        getSupportActionBar().setTitle("Editar perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogData = ProgressDialog.show(this,
            "",
            "Recuperando dados",
            true);



        carregar();

    }

    public void carregar(){
        //acessar a referencia do nó usuarios e seu filho(usuario logados)

DatabaseReference usuario = referencia.child("usuarios").child(auth.getUid());
        usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nome = findViewById(R.id.editar_nome);
                email = findViewById(R.id.editar_email);
                rbMasc = findViewById(R.id.radioMasc);
                rbFemin = findViewById(R.id.radioFem);

                //recupera os dados do no e coloca no objeto usuario
                Usuario u = dataSnapshot.getValue(Usuario.class);

                nome.setText(u.getNome());
                email.setText(u.getEmail());

                if(u.getSexo().equals("Masculino")){
                    rbMasc.setChecked(true);
                    rbFemin.setChecked(false);

                }else{
                    rbFemin.setChecked(true);
                    rbMasc.setChecked(false);
                }

                user.setNome(u.getNome());
                user.setEmail(u.getEmail());
                user.setId(u.getId());
                user.setFoto(u.getFoto());
                user.setSenha(u.getSenha());
                user.setSexo(u.getSexo());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textView = findViewById(R.id.textEdit);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarFoto();
            }
        });

        final StorageReference imagemRef = storageReference
            .child("imagens")
            .child("perfil")
            .child(auth.getUid())
            .child("perfil.jpeg");

        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Picasso.get()
                    .load(downloadUrl.toString())
                    .into(imagemPerfil);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                final Bitmap nullImage = BitmapFactory.decodeResource(getResources(), R.drawable.padrao);
                imagemPerfil.setImageBitmap(nullImage);
            }
        });

        //Fecha a barra de progresso
        dialogData.dismiss();


    }

    public void mudarFoto(){
        final String[] modes = new String[]{"Tirar da camera","Selecione da galeria"};

        //Cria uma AlertDialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        //Seta o título
        builder.setTitle("Selecionar uma foto")
            //Seta os itens de opção
            .setItems(modes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Verifica o índice do item
                    switch (i){
                        //Se o usuário escolheu tirar uma foto com a camera
                        case 0:
                        if (getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(intent.resolveActivity(getPackageManager()) != null){
                                startActivityForResult(intent, SELECAO_CAMERA);
                            }
                        }else{
                            //alertCameraNotFound();
                        }
                        break;
                        //Se o usuário escolheu selecionar foto pela galeria
                        case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if(intent.resolveActivity(getPackageManager()) != null){
                            startActivityForResult(intent, SELECAO_GALERIA);
                        }
                        break;
                    }
                }
            })
            .create()
            .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imagemPerfil = findViewById(R.id.imageProfile);

        if(resultCode == RESULT_OK){


            try{
                switch(requestCode){
                    case SELECAO_CAMERA:
                    imagem = (Bitmap) data.getExtras().get("data");
                    break;
                    case SELECAO_GALERIA:
                    Uri localImagemSelecionada = data.getData();
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada );
                    break;
                }

                if( imagem != null){
                    //recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    progressImage.setVisibility(View.VISIBLE);
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar no Firebase
                    final StorageReference imagemRef = storageReference
                        .child("imagens")
                        .child("perfil")
                        .child(auth.getUid())
                        .child("perfil.jpeg");

                    final UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Se houve erro no upload da imageFile
                            Toast.makeText(PerfilActivity.this, "Erro ao carregar a foto", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                            //Se o upload da imageFile foi realizado com sucesso
                            imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    user.setFoto(downloadUrl.toString());
                                    imagemPerfil.setImageBitmap(imagem);

                                    Toast.makeText(PerfilActivity.this, "Foto alterada com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            //Seta a visibilidade como invisível
                            progressImage.setVisibility(View.GONE);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void save(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario u = new Usuario();
                u.setEmail(user.getEmail());
                u.setNome(user.getNome());
                u.setSexo(user.getSexo());
                u.setSenha(user.getSenha());
                u.setId(user.getId());
                u.setFoto(user.getFoto());

                databaseReference.child(user.getId()).setValue(u);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


*/




