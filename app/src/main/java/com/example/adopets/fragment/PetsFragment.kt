package com.example.adopets.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import com.example.adopets.R
import com.example.adopets.activity.CadAnimalActivity
import com.example.adopets.adapter.AnimalAdapter
import com.example.adopets.adapter.TabsAdapter
import com.example.adopets.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_pets.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PetsFragment : Fragment() {

    private lateinit var btn_animal: Button
    private lateinit var tabs_animais: TabLayout
    private lateinit var viewPagerTabs: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_pets, container, false)

        viewPagerTabs = view.findViewById(R.id.viewPagerTabs)
        tabs_animais = view.findViewById(R.id.tabs_animais)

        val fragmentAdapter = TabsAdapter(childFragmentManager)
        //supportFragmentManager

        viewPagerTabs.adapter = fragmentAdapter
        tabs_animais.setupWithViewPager(viewPagerTabs)

        //tabs()
        btn_animal = view.findViewById(R.id.add)
        btn_animal.setOnClickListener {
            startActivity(Intent(context, CadAnimalActivity::class.java))
        }



        return view
    }


}
