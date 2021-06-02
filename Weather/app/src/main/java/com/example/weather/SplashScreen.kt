package com.example.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class SplashScreen : AppCompatActivity() {
    lateinit var mfusedlocation: FusedLocationProviderClient
    private var myRequestCode=1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mfusedlocation= LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(CheckPermission()){
            //will check if permission is already given or set earler
            if(LocationEnable()){
                // will check if the GPS is turned on from nevigationn bar
                mfusedlocation.lastLocation.addOnCompleteListener{
                    task->
                    var location: Location?=task.result
                    if(location==null)
                    {
                        NewLocation()
                    }else{
                        Handler(Looper.getMainLooper()).postDelayed(
                        {
                        val intent = Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },2000)
                    }
                }
            }else{
                Toast.makeText(this, "please Turn on your GPS location",Toast.LENGTH_LONG).show()

            }
            
        }else{
            //as permission is not given go will ask for permission
            RequestPermission()
        }
    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {
        var locationRequest=LocationRequest()
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1
        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
    }
    private val locationCallback=object:LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {

                var lastLocation:Location=p0.lastLocation
        }
    }

    private fun LocationEnable(): Boolean {
        var locationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun CheckPermission(): Boolean {
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) ==PackageManager.PERMISSION_GRANTED )
        {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==myRequestCode){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation()
            }
        }
    }
}