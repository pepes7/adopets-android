package com.example.adopets.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.example.adopets.R
import com.example.adopets.fragment.AjustesFragment
import com.example.adopets.fragment.MapaFragment
import com.example.adopets.fragment.PerfilFragment
import com.example.adopets.fragment.PetsFragment
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import kotlinx.android.synthetic.main.botton_navigation.*

class NavigationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var  botton : BottomNavigationViewEx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        inicializarComponentes()

        //Habilita a navegação e realiza a troca de fragments
        habilitarNavegacao(botton)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
