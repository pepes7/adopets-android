package com.example.adopets.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.adopets.fragment.AdotadosFragment
import com.example.adopets.fragment.AjudadosFragment
import com.example.adopets.fragment.DoadosFragment

class TabsAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {
                DoadosFragment()
            }
            1 ->
                AdotadosFragment()

            else -> {
                return AjudadosFragment()
            }
        }
    }



    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence{
            return when(position){
                0 -> "Doados"
                1 -> "Adotados"
                else -> {
                    return "Ajudados"
                }
            }

    }

}