package com.example.assignment2

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment2.databinding.ActivityShowAllFilesBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ShowAllFiles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShowAllFilesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Show All Files"

        var id = 1
        val dialog = ProgressDialog(this)
        dialog.setMessage("Loading...")
        dialog.setCancelable(false)
        dialog.show()

        val storage = Firebase.storage
        val listRef = storage.reference.child("Files")

        val files = ArrayList<FileModel>()
        val adapter = FileAdapter(this,files)
        binding.rvFiles.layoutManager = LinearLayoutManager(this)

        listRef.listAll()
            .addOnSuccessListener {
                for (file in it.items){
                    file.downloadUrl.addOnSuccessListener {uri ->
                        files.add(FileModel("File $id",uri.toString()))
                        binding.rvFiles.adapter  = adapter
                        id++
                    }
                }
                if (dialog.isShowing) dialog.dismiss()
            }
            .addOnFailureListener {
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(this, "Error while fetching files", Toast.LENGTH_SHORT).show()
            }
    }
}