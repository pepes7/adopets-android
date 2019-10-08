package com.example.adopets.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.content.Intent
import android.graphics.Color
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.maps.model.*
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.widget.Button
import com.example.adopets.R
import com.example.adopets.activity.ListagemTodosAnimaisActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MapaFragment : Fragment() {

    private var currentMarker: Marker? = null
    private lateinit var  btn_animal: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_mapa, container, false)

        btn_animal = rootView.findViewById(R.id.add)

        btn_animal.setOnClickListener {
            startActivity(Intent(context, ListagemTodosAnimaisActivity::class.java))
        }

        val mapFragment =
            childFragmentManager.findFragmentById(com.example.adopets.R.id.frg) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear() //clear old markers

            val lugar = CameraPosition.builder()
                .target(LatLng(-3.0589489,-59.9930218))
                .zoom(11.5F)
                .bearing(0f)
                .tilt(45f)
                .build()

//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(lugar), 5000, null)

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(lugar), 1, null)

            mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    currentMarker = marker

                    val builder = AlertDialog.Builder(activity)
                    with(builder) {
                        setTitle("O que você deseja fazer com este animal?")
                        setPositiveButton("Adotar", null)
                        setNegativeButton("Ajudar", null)
                        setNeutralButton("Cancelar", null)
                    }

                    val alertDialog = builder.create()
                    alertDialog.show()

                    val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    with(button) {
                        setPadding(0, 0, 20, 0)
                        setTextColor(Color.RED)
                    }

                    val button2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    with(button2) {
                        setPadding(0, 0, 40, 0)
                        setTextColor(Color.BLUE)
                    }

                    val button3 = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                    with(button3) {
                        setPadding(0, 0, 20, 0)
                        setTextColor(Color.DKGRAY)
                    }

                    return false
                }
            })

            mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                override fun onMapClick(latLng: LatLng) {
                    currentMarker = null
                }
            })

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(-3.074601, -60.039474))
                    .title("Mel")
                    .icon(this.context?.let{bitmapDescriptorFromVector(it, com.example.adopets.R.drawable.mel_perfil)})
            )

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(-3.115378, -59.977482))
                    .title("Cão 2")
                    .snippet("Talentoso")
            )

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(-3.134159, -60.013139))
                    .title("Gato")
            )
        }

        return rootView
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}
