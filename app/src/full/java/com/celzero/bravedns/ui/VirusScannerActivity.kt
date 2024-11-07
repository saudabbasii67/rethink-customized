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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.InputStream


class VirusScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virus_scanner)

        // Call the function to read JSON and print hash values
        readVirusDbJson()

        // Call the function to list installed packages and print package names
        listInstalledPackages()

        // Log each installed package with its SHA-256 hash
        logPackagesWithHashes()

        // Compare hashes and detect threats
        scanForThreats()
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
//                Log.d("VirusScanner", "Hash: $hashValue") // Print each hash value
                virusList.add(VirusEntry(hash = hashValue, tags = tags))
            }

        } catch (e: Exception) {
            e.printStackTrace()
//            Log.e("VirusScanner", "Error reading virus_DB.json: ${e.message}")
        }
        return virusList
    }

    private fun listInstalledPackages() {
        try {
            val packageManager = packageManager
            val packages: List<PackageInfo> = packageManager.getInstalledPackages(0)

            for (packageInfo in packages) {
                val packageName = packageInfo.packageName
//                Log.d("VirusScanner", "Package Name: $packageName")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("VirusScanner", "Error listing installed packages: ${e.message}")
        }
    }

    private fun logPackagesWithHashes() {
        val packageManager = packageManager
        val installedPackages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        installedPackages.forEach { packageInfo ->
            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            val apkFilePath = packageInfo.applicationInfo.sourceDir
            val sha256Hash = calculateSHA256Hash(apkFilePath)

//            Log.d("VirusScanner Hash Map", "$appName | $sha256Hash")
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

    private fun scanForThreats() {
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
                else {
                    Log.d("VirusScanner", "Safe APP: $appName")
                }
            }
        }

        Log.d("VirusScanner", "Total Threats Found: $threatCount")
        Log.d("VirusScanner", "Total Apps Scanned: $totalAppsCounter")
    }

    data class VirusEntry(val hash: String, val tags: String)
}


