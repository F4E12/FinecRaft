package com.example.qualif

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qualif.database.DatabaseHelper
import com.example.qualif.databinding.ActivityRegisterBinding
import com.example.qualif.model.User

class RegisterActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        databaseHelper = DatabaseHelper(this)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text
            val email = binding.etEmail.text
            val password = binding.etPassword.text
            val phone = binding.etPhone.text
            val cbTnc = binding.cbTnc.isChecked

            if (email.isEmpty()) {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            }else if (username.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            }else if (password.isEmpty()) {
                Toast.makeText(this, "Phone cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!cbTnc) {
                Toast.makeText(this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure want to register?")

                builder.setPositiveButton("Yes") { _, _ ->
                    val newUser = User(0, username.toString(), email.toString(), password.toString(), phone.toString())
                    if (databaseHelper.insertUser(newUser)) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show()
                    }
                }

                builder.setNegativeButton("No") { dialog, _ -> {
                    dialog.dismiss()
                }}

                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}