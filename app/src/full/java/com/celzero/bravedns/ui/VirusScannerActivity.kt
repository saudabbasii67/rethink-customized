package com.celzero.bravedns.ui

import android.content.pm.PackageInfo
import android.content.pm.PackageManager // Add this import
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.celzero.bravedns.R
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.MessageDigest

class VirusScannerActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var totalAppsText: TextView
    private lateinit var threatCountText: TextView
    private lateinit var currentAppText: TextView

    private var totalAppsCounter = 0
    private var threatCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virus_scanner)

        // Initialize the views
        progressBar = findViewById(R.id.progressBar)
        totalAppsText = findViewById(R.id.total_apps_text)
        threatCountText = findViewById(R.id.threat_count_text)
        currentAppText = findViewById(R.id.current_app_text)

        // Start the scanning in a background thread
        startScanInBackground()
    }

    private fun startScanInBackground() {
        // Show progress bar and update UI dynamically
        progressBar.visibility = ProgressBar.VISIBLE

        // Run the scanning in a coroutine to not block the UI thread
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                scanForThreats()
            }
        }
    }

    private fun readVirusDbJson(): List<VirusEntry> {
        val virusList = mutableListOf<VirusEntry>()
        try {
            val inputStream = assets.open("test.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val jsonText = bufferedReader.use { it.readText() }

            val jsonObject = JSONObject(jsonText)
            val dataArray: JSONArray = jsonObject.getJSONArray("data")

            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val hashValue = item.getString("hash")
                val tags = item.getString("tags")
                virusList.add(VirusEntry(hash = hashValue, tags = tags))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return virusList
    }

    private fun calculateSHA256Hash(filePath: String): String {
        return try {
            val file = File(filePath)
            val digest = MessageDigest.getInstance("SHA-256")
            val inputStream = file.inputStream()

            inputStream.use { stream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }

            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            "Hash calculation error"
        }
    }

    private fun scanForThreats() {
        val virusDB = readVirusDbJson()
        val packageManager = packageManager
        val installedPackages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        installedPackages.forEach { packageInfo ->
            // Update the UI to show the number of apps being scanned
            totalAppsCounter++
            runOnUiThread {
                totalAppsText.text = "Total Packages Scanned: $totalAppsCounter"
            }

            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            val apkFilePath = packageInfo.applicationInfo.sourceDir
            val packageHash = calculateSHA256Hash(apkFilePath)

            // Update the UI to show the current app being scanned
            runOnUiThread {
                currentAppText.text = "Currently Scanning: $appName"
            }

            virusDB.forEach { virusEntry ->
                if (virusEntry.hash.equals(packageHash, ignoreCase = true)) {
                    // Threat detected, update threat count
                    threatCount++
                    runOnUiThread {
                        threatCountText.text = "Total Threats Found: $threatCount"
                    }

                    Log.d("VirusScanner", "Threat Detected!")
                    Log.d("VirusScanner", "App Name: $appName")
                    Log.d("VirusScanner", "Package Hash: $packageHash")
                    Log.d("VirusScanner", "Virus DB Hash: ${virusEntry.hash}")
                    Log.d("VirusScanner", "Tags: ${virusEntry.tags}")
                }
            }
        }

        // Hide progress bar after scanning
        runOnUiThread {
            progressBar.visibility = ProgressBar.GONE
        }
    }

    data class VirusEntry(val hash: String, val tags: String)
}
