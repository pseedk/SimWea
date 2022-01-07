package com.example.simwea

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.simwea.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather?q=%s&APPID=963c3f3af06a707401a2dc1a28599a8e&lang=ru&units=metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonShowWeather.setOnClickListener {
            var city = binding.editTextCity.text.toString().trim()
            if (city.isNotEmpty()) {
                var task = GetURLData()
                var url = String.format(WEATHER_URL, city)
                task.execute(url)
            }
        }

    }

    private inner class GetURLData : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            binding.editTextCity.setText("Ожидайте...")

        }

        override fun doInBackground(vararg params: String?): String? {
            var url: URL?
            var result = StringBuilder()
                url = URL(params[0])
                var urlConnection: HttpURLConnection? = url.openConnection() as HttpURLConnection?
                var inputStream: InputStream? = urlConnection?.inputStream
                var inputStreamReader = InputStreamReader(inputStream)
                var reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    result.append(line)
                    line = reader.readLine()
                }
                return result.toString()
           // urlConnection?.disconnect()
            }





        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var jsonObject = JSONObject(result)
            var city = jsonObject.getString("name")
            var temp = jsonObject.getJSONObject("main").getString("temp")
            var description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")
            var weather = String.format("%s\nТемпература: %s\nНа улице: %s", city, temp, description)
            binding.textViewWeather.setText(weather)
        }
    }

}



