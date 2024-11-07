package com.celzero.bravedns.ui

import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.celzero.bravedns.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import android.content.pm.PackageManager
import java.io.File
import java.security.MessageDigest


class VirusScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virus_scanner)

        // Call the function to read JSON and print hash values
        readVirusDbJson()

        // Call the function to list installed packages and print package names
        listInstalledPackages()

        logPackagesWithHashes()
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

    // This function lists all installed packages and logs each package name with its SHA-256 hash
    private fun logPackagesWithHashes() {
        val packageManager = packageManager
        val installedPackages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        installedPackages.forEach { packageInfo ->
            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            val apkFilePath = packageInfo.applicationInfo.sourceDir

            // Calculate SHA-256 hash for the APK file
            val sha256Hash = calculateSHA256Hash(apkFilePath)

            // Log the app name along with its hash
            Log.d("VirusScanner Hash Map: ", "$appName | $sha256Hash")
        }
    }

    // This function calculates the SHA-256 hash of a file at the specified path
    private fun calculateSHA256Hash(filePath: String): String {
        try {
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

            // Convert the hash bytes to a hex string
            return digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("VirusScanner", "Error calculating SHA-256 hash for $filePath", e)
            return "Hash calculation error"
        }
    }

}
