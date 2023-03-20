package com.example.assignment2

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.databinding.FileItemBinding

class FileAdapter(var context: Context, var data: ArrayList<FileModel>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(var binding: FileItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = FileItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.binding.fileName.text = data[position].name
        holder.binding.btnDownload.setOnClickListener {
            downloadFile(data[position].url,position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun requestPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions((context as Activity), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    private fun downloadFile(urlDownload:String,position: Int){
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Toast.makeText(context, "Download File...", Toast.LENGTH_LONG).show()
            val myRequest = DownloadManager.Request(Uri.parse(urlDownload))
            val cookie = CookieManager.getInstance().getCookie(urlDownload)
            myRequest.addRequestHeader("cookie",cookie)
            myRequest.setDescription("Download File...")
            myRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${data[position].name}.pdf")
            myRequest.setTitle("${data[position].name}.pdf")
            myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val dm = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(myRequest)
        }else{
            requestPermission()
        }
    }
}