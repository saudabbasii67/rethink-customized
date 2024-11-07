package com.celzero.bravedns.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.celzero.bravedns.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class VirusScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virus_scanner)

        // Call the function to read JSON and print hash values
        readVirusDbJson()
    }

    private fun readVirusDbJson() {
        try {
            // Open the JSON file from the assets folder
            val inputStream = assets.open("virus_DB.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }

            // Parse the JSON content
            val jsonObject = JSONObject(jsonText)
            val dataArray = jsonObject.getJSONArray("data")

            // Loop through each entry in the data array and print the hash value
            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val hashValue = item.getString("hash")
                println("VirusScannerHash : " + hashValue)
                Log.d("VirusScanner", "Hash: $hashValue") // Print each hash value
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("VirusScanner", "Error reading virus_DB.json: ${e.message}")
        }
    }
}
