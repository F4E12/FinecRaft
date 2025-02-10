package com.example.qualif

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qualif.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text
            val password = binding.etPassword.text
            val rbMale = binding.rbMale.isChecked
            val rbFemale = binding.rbFemale.isChecked
            val cbTnc = binding.cbTnc.isChecked

            if (username.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!rbMale && !rbFemale) {
                Toast.makeText(this, "Gender must be selected", Toast.LENGTH_SHORT).show()
            } else if (!cbTnc) {
                Toast.makeText(this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure want to register?")

                builder.setPositiveButton("Yes") { dialog, which ->
                    val intent = Intent(this,  MainActivity::class.java)
                    startActivity(intent)
                }

                builder.setNegativeButton("No") { dialog, which -> {
                    dialog.dismiss()
                }}

                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}