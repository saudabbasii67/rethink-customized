package com.celzero.bravedns.ui

import android.content.pm.PackageInfo
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

        // Call the function to list installed packages and print package names
        listInstalledPackages()
    }

    private fun readVirusDbJson() {
        try {
            val inputStream = assets.open("virus_DB.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }

            val jsonObject = JSONObject(jsonText)
            val dataArray = jsonObject.getJSONArray("data")

            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val hashValue = item.getString("hash")
                Log.d("VirusScanner", "Hash: $hashValue") // Print each hash value
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("VirusScanner", "Error reading virus_DB.json: ${e.message}")
        }
    }

    private fun listInstalledPackages() {
        try {
            // Get PackageManager instance
            val packageManager = packageManager
            // Get list of all installed packages
            val packages: List<PackageInfo> = packageManager.getInstalledPackages(0)

            // Print each package name
            for (packageInfo in packages) {
                val packageName = packageInfo.packageName
                Log.d("VirusScanner", "Miji Package Name: $packageName")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("VirusScanner", "Error listing installed packages: ${e.message}")
        }
    }
}
