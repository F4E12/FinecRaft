package com.example.qualif

import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qualif.adapter.UserAdapter
import com.example.qualif.database.DatabaseHelper
import com.example.qualif.databinding.ActivityAdminBinding
import com.example.qualif.databinding.ActivitySmsBinding
import com.example.qualif.model.User

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userAdapter: UserAdapter
    private var userList: List<User> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS),
                100
            )
        }

        databaseHelper = DatabaseHelper(this)

        binding.btnAddUser.setOnClickListener { addUser() }
        binding.btnUpdateUser.setOnClickListener { updateUser() }
        binding.btnDeleteUser.setOnClickListener { deleteUser() }

        binding.btnSendBroadcast.setOnClickListener {
            val message = binding.etBroadcastMessage.text.toString().trim()

            if (message.isEmpty()) {
                Toast.makeText(this, "Broadcast message cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendBroadcastSMS(message)
        }
        setupRecyclerView()
        loadUsers()
    }

    private fun setupRecyclerView() {
        binding.rvUserList.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList)
        binding.rvUserList.adapter = userAdapter
    }

    private fun loadUsers() {
        userList = databaseHelper.getUsers()
        userAdapter = UserAdapter(userList)
        binding.rvUserList.adapter = userAdapter
    }

    private fun addUser() {
        val user = User(
            0,
            binding.etUserName.text.toString(),
            binding.etUserEmail.text.toString(),
            binding.etUserPassword.text.toString(),
            binding.etUserPhone.text.toString()
        )

        if (databaseHelper.insertUser(user)) {
            Toast.makeText(this, "User Added!", Toast.LENGTH_SHORT).show()
            loadUsers()
        } else {
            Toast.makeText(this, "Failed to Add User", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUser() {
        val userIdStr = binding.etUserId.text.toString().trim()
        if (userIdStr.isEmpty()) {
            Toast.makeText(this, "User ID is required!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = userIdStr.toInt()
        val user = User(
            userId,
            binding.etUserName.text.toString(),
            binding.etUserEmail.text.toString(),
            binding.etUserPassword.text.toString(),
            binding.etUserPhone.text.toString()
        )

        if (databaseHelper.updateUser(user)) {
            Toast.makeText(this, "User Updated!", Toast.LENGTH_SHORT).show()
            loadUsers()
        } else {
            Toast.makeText(this, "Failed to Update User", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteUser() {
        val userIdStr = binding.etUserId.text.toString().trim()

        if (userIdStr.isEmpty()) {
            Toast.makeText(this, "User ID is required!", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = userIdStr.toInt()

        if (databaseHelper.deleteUser(userId)) {
            Toast.makeText(this, "User Deleted!", Toast.LENGTH_SHORT).show()
            loadUsers() // Refresh the list
        } else {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendBroadcastSMS(message: String) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.SEND_SMS), 101
            )
            return
        }

        val phoneNumbers = databaseHelper.getAllUserPhones()

        if (phoneNumbers.isEmpty()) {
            Toast.makeText(this, "No users found!", Toast.LENGTH_SHORT).show()
            return
        }

        val smsManager = SmsManager.getDefault()
        for (phoneNumber in phoneNumbers) {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        }

        Toast.makeText(this, "Broadcast message sent to all users!", Toast.LENGTH_SHORT).show()
    }
}
