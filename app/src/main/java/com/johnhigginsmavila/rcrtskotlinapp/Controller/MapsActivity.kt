package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportForm
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.EXTRA_LOCATION
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.EXTRA_MAP_CALLED_FROM
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.NEW_REPORT
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMapClickListener,
    GoogleApiClient.ConnectionCallbacks {
    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var postion: MarkerOptions
    private lateinit var m: Marker

    private lateinit var calledFrom: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        calledFrom = intent.getStringExtra(EXTRA_MAP_CALLED_FROM)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        val point = LatLng(14.6527531, 120.9824008)
        postion = MarkerOptions().position(point).title("Marker in Sydney")
        m = map.addMarker(postion)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f))

        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener{
            true
        }
        map.setOnMapClickListener {
            postion.position(it)
            setMarker(it)

        }

        mapPinBtn.setOnClickListener{
            mapPinBtnOnClicked()

            // Toast.makeText(this, "I Clicked here at ${pos.longitude}, ${pos.latitude}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapClick(p0: LatLng?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setMarker (pos: LatLng) {
        m.position = pos
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    fun mapPinBtnOnClicked () {
        when (calledFrom) {
            NEW_REPORT -> goToNewReport()
            else -> goToNewReport()
        }
    }

    fun goToNewReport () {
        val pos = postion.position

        val menuActivity = Intent(this, MenuActivity::class.java)

        ReportForm.lat = pos.latitude
        ReportForm.long = pos.longitude
        menuActivity.putExtra(EXTRA_LOCATION, pos)

        startActivity(menuActivity)
        finish()
    }


}

