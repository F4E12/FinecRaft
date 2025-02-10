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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.qualif.adapter.TabHomeVPAdapter
import com.example.qualif.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)

        val sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE)
        val darkTheme = sharedPreferences.getBoolean("isDarkTheme", false)

        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchTheme.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchTheme.isChecked = false
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkTheme", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkTheme", false)
            }
            editor.apply()
        }
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)

        binding.vpTabHome.adapter = TabHomeVPAdapter(this)

        val username = intent.getStringExtra("username")

        binding.tvHello.text = "Hello, $username"

        TabLayoutMediator(binding.tlTabHome, binding.vpTabHome) { tab, position ->
            tab.text = when(position) {
                0 -> "Items"
                1 -> "Reviews"
                else -> "Items"
            }

            tab.setIcon(when(position) {
                0 -> R.drawable.ic_launcher_background
                1 -> R.drawable.ic_launcher_foreground
                else -> R.drawable.ic_launcher_background
            })
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_profile -> {
                val intent = Intent(this, InternalExternalStorageActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_sms -> {
                val intent = Intent(this, SmsActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_map -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_profile -> Toast.makeText(this, "Profile menu clicked", Toast.LENGTH_SHORT).show()
            R.id.menu_log_out -> Toast.makeText(this, "Log Out menu clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}