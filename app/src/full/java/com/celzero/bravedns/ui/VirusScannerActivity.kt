package com.celzero.bravedns.ui

import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.celzero.bravedns.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.view.View
import android.widget.ProgressBar
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import android.content.pm.PackageManager
import java.io.File
import java.security.MessageDigest
import org.json.JSONArray

class VirusScannerActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar  // Declare the ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virus_scanner)

        // Initialize the ProgressBar
        progressBar = findViewById(R.id.progressBar)

        // Start the scanning process
        startScanning()
    }

    private fun startScanning() {
        // Launch a coroutine to run the scanning process on a background thread
        lifecycleScope.launch {
            // Show the progress bar while scanning
            progressBar.visibility = View.VISIBLE

            // Run the methods asynchronously using coroutines
            listInstalledPackages()
            scanForThreats()

            // Hide the progress bar after scanning is completed
            progressBar.visibility = View.GONE
        }
    }

    private suspend fun readVirusDbJson(): List<VirusEntry> {
        return withContext(Dispatchers.IO) {
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
            return@withContext virusList
        }
    }

    private suspend fun listInstalledPackages() {
        // Run the package scanning task on the IO dispatcher (background thread)
        withContext(Dispatchers.IO) {
            try {
                val packageManager = packageManager
                val packages: List<PackageInfo> = packageManager.getInstalledPackages(0)

                for (packageInfo in packages) {
                    val packageName = packageInfo.packageName
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("VirusScanner", "Error listing installed packages: ${e.message}")
            }
        }
    }

    private suspend fun scanForThreats() {
        // Run the scanning task on the IO dispatcher (background thread)
        withContext(Dispatchers.IO) {
            val virusDB = readVirusDbJson()
            val packageManager = packageManager
            val installedPackages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

            var totalAppsCounter = 0
            var threatCount = 0

            installedPackages.forEach { packageInfo ->
                totalAppsCounter++
                val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                val apkFilePath = packageInfo.applicationInfo.sourceDir
                val packageHash = calculateSHA256Hash(apkFilePath)

                virusDB.forEach { virusEntry ->

                    if (virusEntry.hash.equals(packageHash, ignoreCase = true)) {
                        Log.d("VirusScanner", "Threat Detected!")
                        Log.d("VirusScanner", "App Name: $appName")
                        Log.d("VirusScanner", "Package Hash: $packageHash")
                        Log.d("VirusScanner", "Virus DB Hash: ${virusEntry.hash}")
                        Log.d("VirusScanner", "Tags: ${virusEntry.tags}")
                        threatCount++
                    }
                }
            }

            // Log the total threats found and total apps scanned
            withContext(Dispatchers.Main) {
                Log.d("VirusScanner", "Total Threats Found: $threatCount")
                Log.d("VirusScanner", "Total Apps Scanned: $totalAppsCounter")
            }
        }
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
            Log.e("VirusScanner", "Error calculating SHA-256 hash for $filePath", e)
            "Hash calculation error"
        }
    }

    data class VirusEntry(val hash: String, val tags: String)
}
