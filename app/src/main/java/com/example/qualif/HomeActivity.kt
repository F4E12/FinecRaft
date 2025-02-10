package com.example.qualif

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.qualif.adapter.TabHomeVPAdapter
import com.example.qualif.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)

//        val sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE)
//        val darkTheme = sharedPreferences.getBoolean("isDarkTheme", false)
//
//        if (darkTheme) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            binding.switchTheme.isChecked = true
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            binding.switchTheme.isChecked = false
//        }
//
//        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
//            val editor = sharedPreferences.edit()
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                editor.putBoolean("isDarkTheme", true)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                editor.putBoolean("isDarkTheme", false)
//            }
//            editor.apply()
//        }
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)


        val username = intent.getStringExtra("username")

        binding.tvHello.text = "Hello, $username"

        binding.vpTabHome.adapter = TabHomeVPAdapter(this)

        TabLayoutMediator(binding.tlTabHome, binding.vpTabHome) { tab, position ->
            tab.text = when(position) {
                0 -> "Items"
                1 -> "Map"
                else -> "Items"
            }

            tab.setIcon(when(position) {
                0 -> R.drawable.ic_launcher_background
                1 -> R.drawable.ic_launcher_foreground
                else -> R.drawable.ic_launcher_background
            })
        }.attach()

    }


    private fun getCurrentUsername(): String {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val currentUsername = getCurrentUsername()

        if (currentUsername == "admin") {
            menu?.findItem(R.id.menu_admin)?.isVisible = true
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_admin -> {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_log_out -> {
                logout()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}