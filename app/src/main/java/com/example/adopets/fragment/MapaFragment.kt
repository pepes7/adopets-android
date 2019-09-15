package com.example.adopets.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import android.content.Context
import android.graphics.Canvas

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MapaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(com.example.adopets.R.layout.fragment_mapa, container, false)

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

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(lugar), 5000, null)

            mMap.setOnMapClickListener {
                
            }

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
