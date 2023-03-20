package com.example.assignment2

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.assignment2.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var fileUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialog = ProgressDialog(this)
        dialog.setMessage("Uploading...")
        dialog.setCancelable(false)

        val storage = Firebase.storage
        val reference = storage.reference

        val getFile = registerForActivityResult(ActivityResultContracts.GetContent()){
            fileUri = it!!
            dialog.show()
            reference.child("Files/${UUID.randomUUID()}")
                .putFile(fileUri)
                .addOnSuccessListener {
                    if (dialog.isShowing) dialog.dismiss()
                    Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    if (dialog.isShowing) dialog.dismiss()
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnChooseFile.setOnClickListener {
            getFile.launch("application/pdf")
        }

        binding.btnShowAll.setOnClickListener {
            startActivity(Intent(this,ShowAllFiles::class.java))
        }

    }
}