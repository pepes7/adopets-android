package com.example.adopets.fragment

import android.Manifest
import android.annotation.SuppressLint
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
import android.graphics.Canvas
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*

import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.StaggeredGridLayoutManager
import com.google.android.gms.maps.model.*
import android.util.Log
import android.widget.*


import com.example.adopets.R
import com.example.adopets.activity.*
import com.example.adopets.adapter.AnimalCheckAdapter
import com.example.adopets.helper.Permissao
import com.example.adopets.model.Animal
import com.example.adopets.model.Usuario
import com.example.adopets.utils.animalUtilAll
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_mapa.*

class MapaFragment : Fragment(), OnMapReadyCallback {

    private var currentMarker: Marker? = null
    private lateinit var btn_animal: Button
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val permissaoLocal = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    val firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var database: DatabaseReference


    private lateinit var bparent: RelativeLayout

    //checkpoint, ao clicar lista animais no mesmo local
    private lateinit var btn_check: Button

    //sair da listagem
    private lateinit var btn_volta: ImageView

    //layouts de exibicao para trocar conforme o clicar
    private lateinit var linearDicas: LinearLayout
    private lateinit var linearListaPets: LinearLayout
    private lateinit var adapterAnimal: AnimalCheckAdapter



    //dicas e manuais
    private lateinit var castracao: ImageView
    private lateinit var verao: ImageView
    private lateinit var higiene: ImageView
    private lateinit var agua: ImageView
    private lateinit var ouvido: ImageView
    private lateinit var truque: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_mapa, container, false)
        btn_animal = root.findViewById(R.id.add)


        btn_animal = root.findViewById(R.id.add)

        agua = root.findViewById(R.id.agua)
        ouvido = root.findViewById(R.id.limpezaOuvido)
        truque = root.findViewById(R.id.truques)
        higiene = root.findViewById(R.id.higiene)
        verao = root.findViewById(R.id.verao)
        castracao = root.findViewById(R.id.castracao)

        castracao.setOnClickListener {
            startActivity(Intent(context, DicaCastracaoActivity::class.java))
        }
        verao.setOnClickListener {
            startActivity(Intent(context, DicaVeraoActivity::class.java))
        }
        higiene.setOnClickListener {
            startActivity(Intent(context, DicaHigieneActivity::class.java))
        }

        agua.setOnClickListener {
            startActivity(Intent(context, DicaAguaActivity::class.java))
        }
        ouvido.setOnClickListener {
            startActivity(Intent(context, DicaOuvidoActivity::class.java))
        }
        truque.setOnClickListener {
            startActivity(Intent(context, DicaTruquesActivity::class.java))
        }


        btn_check = root.findViewById(R.id.btn_checkpoint)
        btn_volta = root.findViewById(R.id.voltar)

        linearDicas = root.findViewById(R.id.linkTelasDicas)
        linearListaPets = root.findViewById(R.id.animaisLocal)

        btn_check.setOnClickListener {
            linearDicas.visibility = View.GONE
            linearListaPets.visibility = View.VISIBLE
            //chame a listagem aqui, a lista ja esta visivel
        }

        btn_volta.setOnClickListener {
            linearListaPets.visibility = View.GONE
            linearDicas.visibility = View.VISIBLE
        }


        bparent = root.findViewById(R.id.bottom_sheet_parent)
        var bsBehavior: BottomSheetBehavior<View>
        bsBehavior = BottomSheetBehavior.from(bparent)
        bsBehavior.peekHeight = 100

        val mapFragment = childFragmentManager.findFragmentById(R.id.frg) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return root
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(mMap: GoogleMap?) {
        if (Permissao.validarPermissao(permissaoLocal, activity, 1)) {

            btn_animal.setOnClickListener {
                startActivity(Intent(context, ListagemTodosAnimaisActivity::class.java))
            }

            mMap!!.isMyLocationEnabled = true

            val lugar = CameraPosition.builder()
                .target(LatLng(-3.0589489, -59.9930218))
                .zoom(11.5F)
                .bearing(0f)
                .tilt(45f)
                .build()

//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(lugar), 5000, null)

            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(lugar), 1, null)

            mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    currentMarker = marker

                    linearDicas.visibility = View.GONE
                    linearListaPets.visibility = View.VISIBLE

                    val latitude = marker.position.latitude
                    val longitude = marker.position.longitude

                    val addressList: List<Address>

                    val geocoder = Geocoder(context)

                    addressList = geocoder.getFromLocation(latitude, longitude, 1)

                    var address = addressList[0]

                    Log.d(
                        "adressList-> ",
                        address.postalCode
                    )

                    val cep = address.postalCode

                    val animaisRecuperados =
                        FirebaseDatabase.getInstance().reference.child("animal")

                    var animais = arrayListOf<Animal>()

                    recyclerViewAnimais.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    recyclerViewAnimais.hasFixedSize()
                    adapterAnimal = AnimalCheckAdapter(context!!, animais)
                    recyclerViewAnimais.adapter = adapterAnimal


                    var query = database.child("usuarios").orderByChild("cep").equalTo(cep)
                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val usuario = snapshot.getValue(Usuario::class.java)
                                var query = FirebaseDatabase.getInstance().reference.child("animal")
                                    .orderByChild("doador").equalTo(usuario!!.id)
                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        animais.clear()
                                        for (snapshot in p0.children) {
                                            val animal = snapshot.getValue(Animal::class.java)
                                            animais.add(animal!!)
                                        }
                                        animais.reverse()
                                        adapterAnimal.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(p0: DatabaseError) {

                                    }
                                })
                            }
                        }
                    })
                    return false
                }
            })

            mMap.setOnMapClickListener(
                object : GoogleMap.OnMapClickListener {
                    override fun onMapClick(latLng: LatLng) {
                        currentMarker = null
                    }
                })

            locationManager =
                activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var lat = 0.0
            var long = 0.0

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

//                    mMap.addMarker(
//                        MarkerOptions()
//                            .position(LatLng(lat, long))
//                            .title("Meu local")
//                    )


                }

            }

            locationListener = myLocationListener()
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0F,
                locationListener
            )


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
                            database.child("usuarios").orderByChild("id")
                                .equalTo(animal!!.doador)
                        query.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    val usuario =
                                        snapshot.getValue(Usuario::class.java!!)
                                    var rua = usuario!!.rua
                                    var complemento = usuario!!.complemento
                                    var bairro = usuario!!.bairro
                                    var cep = usuario!!.cep

                                    val location =
                                        "${rua}, ${complemento}, ${bairro}, Amazonas, AM, ${cep}"

                                    Log.d("location-> ", location)

                                    val addressList: List<Address>

                                    val geocoder = Geocoder(context)

                                    addressList =
                                        geocoder.getFromLocationName(location, 1)
                                    if (addressList.isEmpty()) {
                                        Log.d(
                                            "endereço nulo",
                                            "nenhum endereço na lista"
                                        )
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

                                        val latLng =
                                            LatLng(address.latitude, address.longitude)

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
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                //Se ocorrer um erro
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        context,
                        "Erro ao carregar os animais",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            ref.addValueEventListener(postListener)

        }


    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor {
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

}
