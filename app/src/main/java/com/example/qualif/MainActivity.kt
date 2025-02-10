package com.example.qualif

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qualif.database.DatabaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        databaseHelper = DatabaseHelper(this)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        btnLogin.setOnClickListener {
            val username = etUsername.text
            val password = etPassword.text

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username or password cannot be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (databaseHelper.checkUserByUsername(username.toString(), password.toString())) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("username", username.toString()).apply()

                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("username", username.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", null)
        if (savedUsername != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}