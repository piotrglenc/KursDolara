package com.example.kursdolara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kursdolara.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pobierzDane().start()
    }

    private fun pobierzDane(): Thread {
        return Thread {
            val url = URL("https://api.nbp.pl/api/exchangerates/rates/A/USD?format=json")
            val connection = url.openConnection() as HttpsURLConnection
            val inputSR = InputStreamReader(connection.inputStream, "UTF-8")
            val wczytaneDane = Gson().fromJson(inputSR, FetchedData::class.java)
            aktualizujUI(wczytaneDane)
            inputSR.close()
        }
    }

    private fun aktualizujUI(wczytaneDane: FetchedData) {
        runOnUiThread {
            binding.kurs.text = wczytaneDane.rates[0].mid.toString()
            binding.data.text = "Dane na dzień: " + wczytaneDane.rates[0].effectiveDate
        }
    }
}