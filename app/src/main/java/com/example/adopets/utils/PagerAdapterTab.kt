package com.example.adopets.utils

import com.example.adopets.fragment.AdotadosFragment
import com.example.adopets.fragment.AjudadosFragment
import com.example.adopets.fragment.DoadosFragment


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PagerAdapterTab (fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Doados Tab"
            1 -> "Adotados Tab"
            else -> { return "Ajudados Tab"
            }
        }

    }
}
  /*  override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> { DoadosFragment() }
            1 -> AdotadosFragment()
            else -> { return AjudadosFragment()
            }
    }

}*/