package com.example.adopets.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.adopets.R
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import com.example.adopets.model.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var botaoGoogle: SignInButton
    private lateinit var botaoEntrar : Button
    private val RC_CODE_GOOGLE : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //configuração para o login com o Google
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        botaoEntrar = entrar
        botaoGoogle = sign_in_button

        botaoEntrar.setOnClickListener{logar()}
        botaoGoogle.setOnClickListener{telaGoogle()}
    }

    private fun telaGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_CODE_GOOGLE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_CODE_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.i("erro", "Código falho = ${e.getStatusCode()}")
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Sucesso  ao realizar login: ",Toast.LENGTH_SHORT).show()
//                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    Toast.makeText(applicationContext, "Erro  ao realizar login: ",Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun logar() {
        var email = editTextEmail.text.toString()
        var senha = editTextSenha.text.toString()

        var valido = true

        if (email.isEmpty()) {
            editTextEmail.error = "Campo vazio!"
            valido = false
        }

        if (senha.isEmpty()) {
            editTextSenha.error = "Campo vazio!"
            valido = false
        }

        if (valido) {
            auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        val pd = ProgressDialog(this@MainActivity)
                        pd.setMessage("Entrando...")
                        pd.show()
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(
                                applicationContext,
                                "Credenciais inválidas!", Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                applicationContext,
                                "Erro ao realizar login: " + e.message, Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
        }
    }

    fun telaCadastro(view : View){
        startActivity(Intent(applicationContext, CadastroActivity::class.java))
    }

}
