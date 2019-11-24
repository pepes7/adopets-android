package com.example.adopets.fragment

import android.Manifest
import android.annotation.SuppressLint
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
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.net.Uri
import com.google.android.gms.maps.model.*
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.adopets.R
import com.example.adopets.activity.ListagemTodosAnimaisActivity
import com.example.adopets.helper.Permissao
import com.example.adopets.model.Animal
import com.example.adopets.model.Usuario
import com.example.adopets.utils.animalUtilAll
import com.google.firebase.database.*

class MapaFragment : Fragment() {

    private var currentMarker: Marker? = null
    private lateinit var btn_animal: Button
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val permissaoLocal = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    //    private var listener: OnFragmentInteractionListener? = null
    val firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_mapa, container, false)

        Permissao.validarPermissao(permissaoLocal, activity, 1)

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
                .target(LatLng(-3.0589489, -59.9930218))
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

            mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                override fun onMapClick(latLng: LatLng) {
                    currentMarker = null
                }
            })

            locationManager =
                activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var lat: Double = 0.0
            var long: Double = 0.0

            class myLocationListener : LocationListener {
                override fun onProviderEnabled(p0: String?) {
                }

                override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                }

                override fun onProviderDisabled(p0: String?) {
                }

                override fun onLocationChanged(p0: Location?) {
                    Log.d("Localização", "onLocationChanged: " + p0.toString())

                    lat = p0!!.latitude
                    long = p0.longitude

                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lat, long))
                            .title("Meu local")
                    )

                }

            }

            locationListener = myLocationListener()
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0F,
                locationListener
            )

            mMap.clear()

//            mMap.addMarker(
//                MarkerOptions()
//                    .position(LatLng(-3.074601, -60.039474))
//                    .title("Mel")
//                    .icon(this.context?.let {
//                        bitmapDescriptorFromVector(
//                            it,
//                            com.example.adopets.R.drawable.mel_perfil
//                        )
//                    })
//            )
//

            val ref = firebaseDatabase.getReference("animal")
            database = FirebaseDatabase.getInstance().reference
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    animalUtilAll.clear()
                    for (postSnapshot in dataSnapshot.children) {
                        val animal = postSnapshot.getValue(Animal::class.java)
                        var query =
                            database.child("usuarios").orderByChild("id").equalTo(animal!!.doador)
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    val usuario = snapshot.getValue(Usuario::class.java!!)
                                    var rua = usuario!!.rua
                                    var complemento = usuario!!.complemento
                                    var bairro = usuario!!.bairro
                                    var cep = usuario!!.cep

                                    val location =
                                        "${rua}, ${complemento}, ${bairro}, Amazonas, AM, ${cep}"

                                    Log.d("location-> ", location)

                                    val addressList: List<Address>

                                    val geocoder = Geocoder(context)

//                                    try {
                                    addressList = geocoder.getFromLocationName(location, 1)
                                    if (addressList.isEmpty()) {
                                        Log.d("endereço nulo", "nenhum endereço na lista")
                                    } else {
                                        Log.d("endereço preenchida", "lista tem valor")

                                        var address = addressList[0]
                                        //for print
//                                            Log.d(
//                                                "adressList-> ",
//                                                address.locality + " " + address.latitude + " " + address.longitude +
//                                                        address.adminArea + " " + address.countryName + " " + address.extras +
//                                                        " " + address.featureName + " " + address.locale + " " + address.phone +
//                                                        " " + address.postalCode + " " + address.premises + " " + address.subAdminArea +
//                                                        " " + address.subLocality + " " + address.thoroughfare + " " + address.subThoroughfare +
//                                                        " " + address.url
//                                             )

                                        val latLng = LatLng(address.latitude, address.longitude)

                                        mMap.addMarker(
                                            MarkerOptions().position(latLng)
                                                .title("${animal.nome}")
                                                .icon(
                                                    BitmapDescriptorFactory.defaultMarker(
                                                        BitmapDescriptorFactory.HUE_RED
                                                    )
                                                )
                                        )
                                    }
//                                    } catch (e: Exception) {
//                                        e.printStackTrace()
//                                    }

//                                        mMap.moveCamera(
//                                            CameraUpdateFactory.newLatLngZoom(
//                                                latLng,
//                                                14.9f
//                                            )
//                                        )

                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                //Se ocorrer um erro
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Erro ao carregar os animais", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            ref.addValueEventListener(postListener)

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

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao()
            } else {
                //recuperar local usuario
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    10000F,
                    locationListener
                )
            }
        }

    }

    fun alertaPermissao() {
        val builder = android.support.v7.app.AlertDialog.Builder(
            activity!!,
            R.style.Theme_AppCompat_Light_Dialog
        )
        builder.setTitle("Permissões Negadas")
        builder.setMessage("Para a melhor utilização do app é necessário aceitar a permissão")
        builder.setCancelable(false)
        builder.setPositiveButton("Confirmar") { dialogInterface, i -> }
        val dialog = builder.create()
        dialog.show()
    }

//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }
//
//    @Throws(RuntimeException::class)
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }
//
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment AdotadosFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AdotadosFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

}
