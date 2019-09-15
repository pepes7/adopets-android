package com.example.adopets.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.adopets.R
import com.example.adopets.fragment.AjustesFragment
import com.example.adopets.fragment.MapaFragment
import com.example.adopets.fragment.PerfilFragment
import com.example.adopets.fragment.PetsFragment
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import kotlinx.android.synthetic.main.botton_navigation.*

class HomeActivity : AppCompatActivity() {
    private lateinit var  botton : BottomNavigationViewEx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        inicializarComponentes()

        //Habilita a navegação e realiza a troca de fragments
        habilitarNavegacao(botton)
    }

    private fun habilitarNavegacao(viewEx: BottomNavigationViewEx){

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        viewEx.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_pets -> {
                    trocarFragment(PetsFragment())
                    return@setOnNavigationItemSelectedListener true

                }
                R.id.navigation_mapa -> {
                    trocarFragment(MapaFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_perfil -> {
                    trocarFragment(PerfilFragment())
                    return@setOnNavigationItemSelectedListener true

                }
                R.id.navigation_ajustes -> {
                    trocarFragment(AjustesFragment())
                    return@setOnNavigationItemSelectedListener true

                }
            }
            false
        }
    }

    private fun trocarFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.viewPage, fragment).commit()
    }

    private fun inicializarComponentes(){

        botton = botton_navigation

        //Coloca  o primeiro fragment Pets no FrameLayout
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.viewPage, PetsFragment())
        transaction.commit()

    }
}
