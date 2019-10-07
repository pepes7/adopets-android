package com.example.adopets.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.ImageView
import com.example.adopets.R
import com.example.adopets.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_ajustes.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AjustesFragment : Fragment() {

    private lateinit var imgSair: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_ajustes, container, false)

        imgSair = view.findViewById(R.id.imageButton4)

        imgSair.setOnClickListener {
            val user = FirebaseAuth.getInstance()
            user.signOut()

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)

        }

        // Inflate the layout for this fragment
        return view
    }

}
