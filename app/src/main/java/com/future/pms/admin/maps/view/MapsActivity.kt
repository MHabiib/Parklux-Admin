package com.future.pms.admin.maps.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.future.pms.admin.BaseApp
import com.future.pms.admin.R
import com.future.pms.admin.core.base.BaseActivity
import com.future.pms.admin.maps.injection.DaggerMapsComponent
import com.future.pms.admin.maps.injection.MapsComponent
import com.future.pms.admin.maps.presenter.MapsPresenter
import com.future.pms.admin.util.Constants.Companion.LATITUDE
import com.future.pms.admin.util.Constants.Companion.LONGITUDE
import com.future.pms.admin.util.Constants.Companion.REQUEST_LOCATION_PERMISSION
import com.future.pms.admin.util.Constants.Companion.RESULT_LOCATION
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MapsActivity : BaseActivity(), MapsContract, OnMapReadyCallback {
  private var daggerBuild: MapsComponent = DaggerMapsComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: MapsPresenter
  private lateinit var map: GoogleMap
  private var selectedLocation = LatLng(-6.175392, 106.827153) //MONAS

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_maps)
    presenter.attach(this)
    ibBack.setOnClickListener {
      onBackPressed()
    }
    val latitude = intent.extras?.getDouble(LATITUDE)
    val longitude = intent.extras?.getDouble(LONGITUDE)
    if (latitude != null && longitude != null && latitude != 0.0 && longitude != 0.0) {
      selectedLocation = LatLng(latitude, longitude)
    }
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    enableMyLocation()
    val zoomLevel = 15f
    map.uiSettings.isRotateGesturesEnabled = false
    if (selectedLocation != LatLng(-6.175392, 106.827153)) {
      map.addMarker(MarkerOptions().position(selectedLocation).icon(
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
    }
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, zoomLevel))
    presenter.setMapLongClick(map)
    presenter.setMapStyle(map)
    Toast.makeText(this, "Long click on map to select position", Toast.LENGTH_LONG).show()
  }

  override fun setMapLongClick(map: GoogleMap) {
    map.setOnMapLongClickListener { latLng ->
      val snippet = String.format(Locale.getDefault(), "Lat: %1$.5f, Long: %2$.5f", latLng.latitude,
          latLng.longitude)
      map.clear()
      map.addMarker(MarkerOptions().position(latLng).title("Dropped pin").snippet(snippet).icon(
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
      btnSave.visibility = View.VISIBLE
      btnSave.setOnClickListener {
        val returnIntent = Intent()
        returnIntent.putExtra(LATITUDE, latLng.latitude)
        returnIntent.putExtra(LONGITUDE, latLng.longitude)
        setResult(RESULT_LOCATION, returnIntent)
        finish()
      }
    }
  }

  override fun setMapStyle(map: GoogleMap) {
    try {
      if (!map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))) {
        Timber.e("Style parsing failed.")
      }
    } catch (e: Resources.NotFoundException) {
      Timber.e(e, "Can't find style. Error: ")
    }
  }

  private fun isPermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
  }

  @SuppressLint("MissingPermission") private fun enableMyLocation() {
    if (isPermissionGranted()) {
      map.isMyLocationEnabled = true
    } else {
      ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
          REQUEST_LOCATION_PERMISSION)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
      grantResults: IntArray) {
    if (requestCode == REQUEST_LOCATION_PERMISSION) {
      if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
        enableMyLocation()
      }
    }
  }

  override fun onFailed(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }
}
