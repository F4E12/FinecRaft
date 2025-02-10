package com.example.qualif

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qualif.databinding.ActivityInternalExternalStorageBinding
import java.io.File
import java.io.FileOutputStream

class InternalExternalStorageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInternalExternalStorageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInternalExternalStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val internalPath = "data/data/com.example.qualif"
        val internalDirectory = File(internalPath)

//        val externalDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "com.example.qualif")

        checkDir(internalDirectory)

        binding.btnInput.setOnClickListener {
            val fileName = binding.etFileName.text.toString()
            val fileContent = binding.etFileContent.text.toString()

            inputFile(fileName, fileContent, internalDirectory)
        }

        binding.btnShow.setOnClickListener {
            val fileName = binding.etFileNameToBeShown.text.toString()

            showFileContent(fileName, internalDirectory)
        }

    }

    private fun checkDir(directory : File){
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    private fun inputFile(fileName: String, fileContent: String, directory: File) {
        if (fileName.isEmpty() || fileContent.isEmpty()) {
            Toast.makeText(this, "File name and content cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val newFile = File(directory, fileName)

        val fileOutput = FileOutputStream(newFile)
        fileOutput.write(fileContent.toByteArray())
        fileOutput.close()

        binding.etFileName.text.clear()
        binding.etFileContent.text.clear()
        Toast.makeText(this, "Input file success", Toast.LENGTH_SHORT).show()
    }

    private fun showFileContent(fileName: String, directory: File) {
        if (fileName.isEmpty()) {
            Toast.makeText(this, "File name cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedFile = File(directory, fileName)

        if (!selectedFile.exists()) {
            Toast.makeText(this, "File name doesn't exist", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedFileContent = selectedFile.readText()
        Toast.makeText(this, "File content of $fileName: $selectedFileContent", Toast.LENGTH_SHORT).show()
    }

}