
package com.example.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat=intent.getStringExtra("lat")
        val long=intent.getStringExtra("long")
        Toast.makeText(this,lat+" "+long,Toast.LENGTH_LONG).show()
        window.statusBarColor= Color.parseColor("#1383C3")
        getJsonData(lat,long)
    }

    private fun getJsonData(lat: String?, long: String?) {

        val API_KEY="c3fbd25a4d22b81acb8acabe3bc3760e"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"
        val JsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener {Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()})

        queue.add(JsonRequest)
    }

    private fun setValues(response:JSONObject){
        city.text=response.getString("name")
        var lat= response.getJSONObject("coord").getString("lat")
        var long=response.getJSONObject("coord").getString("long")
        coordinates.text="${lat}, ${long}"
        weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr=response.getJSONObject("main").getString("temp")
        tempr=((((tempr).toFloat()-273.15).toInt()).toString())
        temp.text="${tempr}°C"
        var mintemp=response.getJSONObject("main").getString("temp_min")
        mintemp= ((((mintemp).toFloat()-273.15).toInt()).toString())
        min_temp.text=mintemp+"°C"
        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp= ((((maxtemp).toFloat()-273.15).toInt()).toString())
        max_temp.text=maxtemp+"°C"
        pressure.text=response.getJSONObject("main").getString("pressure")
        humidity.text=response.getJSONObject("main").getString("humidity")+"%"
        wind.text=response.getJSONObject("wind").getString("speed")
        degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"


    }
}