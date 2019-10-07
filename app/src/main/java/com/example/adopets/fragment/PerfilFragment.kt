package com.example.adopets.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.adopets.R
import com.example.adopets.activity.CadastroActivity
import com.example.adopets.activity.EditarPerfilActivity
import com.example.adopets.activity.MainActivity
import com.example.adopets.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.fragment_perfil.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PerfilFragment : Fragment() {

    private lateinit var btn_editar: Button
    private lateinit var btn_apagar: Button

    private lateinit var database: DatabaseReference
    private lateinit var txt_nome: TextView
    private lateinit var txt_email: TextView
    private lateinit var txt_endereco: TextView
    private lateinit var txt_telefone: TextView
    private lateinit var txt_dataNasc: TextView
    private lateinit var imagemPerfil : CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_perfil, container, false)

        database = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser

        btn_editar = view.findViewById(R.id.button)
        btn_apagar = view.findViewById(R.id.button2)

        imagemPerfil = view.findViewById(R.id.foto_perfil)


        btn_editar.setOnClickListener {
            val intent = Intent(activity, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        btn_apagar.setOnClickListener {

            user?.let {
                val email = user.email
                txt_email = view.findViewById(R.id.textView8)
                txt_email.text = email
                var query: Query = database.child("usuarios").orderByChild("email").equalTo(email)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        //Passar os dados para a interface grafica
                        for (snapshot in dataSnapshot.getChildren()) {
                            snapshot.ref.removeValue()
                            user.delete()
                            startActivity(Intent(activity, MainActivity::class.java))
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        //Se ocorrer um erro
                    }
                })

            }

        }

        user?.let {
            val email = user.email
            txt_email = view.findViewById(R.id.textView8)
            txt_email.text = email
            var query: Query = database.child("usuarios").orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //Passar os dados para a interface grafica
                    for (snapshot in dataSnapshot.getChildren()) {
                        val usuario = snapshot.getValue(Usuario::class.java!!)
                        txt_nome = view.findViewById(R.id.textView7)
                        txt_endereco = view.findViewById(R.id.textView9)
                        txt_telefone = view.findViewById(R.id.textView10)
                        txt_dataNasc = view.findViewById(R.id.textView11)

                        txt_nome.text = usuario?.nome
                        txt_endereco.text =
                            usuario?.rua + ", " + usuario?.numero + ", " + usuario?.bairro
                        txt_telefone.text = usuario?.telefone
                        txt_dataNasc.text = usuario?.dataNasc

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

        // Inflate the layout for this fragment
        return view
    }

}

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
