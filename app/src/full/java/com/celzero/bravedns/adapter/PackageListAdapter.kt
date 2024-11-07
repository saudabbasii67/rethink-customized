package com.celzero.bravedns.adapter

import android.content.pm.PackageInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.celzero.bravedns.R

class PackageListAdapter(private val packages: List<PackageInfo>) :
    RecyclerView.Adapter<PackageListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val packageNameTextView: TextView = view.findViewById(R.id.package_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageInfo = packages[position]
        holder.packageNameTextView.text = packageInfo.packageName
    }

    override fun getItemCount() = packages.size
}