package com.example.qualif.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.qualif.model.Item
import com.example.qualif.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "finecraft.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createItemsTable = """
            CREATE TABLE IF NOT EXISTS items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                price REAL NOT NULL,
                compatible_minecraft_version TEXT NOT NULL
            )
        """.trimIndent()

        val createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                phone TEXT NOT NULL
            )
        """.trimIndent()

        db?.execSQL(createItemsTable)
        db?.execSQL(createUsersTable)
    }

    fun getItems(): List<Item> {
        val items = arrayListOf<Item>()
        val db = readableDatabase
        val query = "SELECT * FROM items"

        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (cursor.count > 0) {
            do {
                val item = Item(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("compatible_minecraft_version"))
                )
                items.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return items
    }

    fun insertItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
            put("price", item.price)
            put("compatible_minecraft_version", item.compatibleMinecraftVersion)
        }
        db.insert("items", null, values)
        db.close()
    }

    fun updateItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
            put("price", item.price)
            put("compatible_minecraft_version", item.compatibleMinecraftVersion)
        }
        db.update("items", values, "id = ?", arrayOf(item.id.toString()))
        db.close()
    }

    fun deleteItem(id: String) {
        val db = writableDatabase
        db.delete("items", "id = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllItems() {
        val db = writableDatabase
        db.delete("items", null, null)
        db.close()
    }

    fun checkUserByUsername(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE name = ? AND password = ?",
            arrayOf(username, password)
        )
        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }

    fun getUsers(): List<User> {
        val users = arrayListOf<User>()
        val db = readableDatabase
        val query = "SELECT * FROM users"

        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (cursor.count > 0) {
            do {
                val user = User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }

    fun insertUser(user: User): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", user.name)
                put("email", user.email)
                put("password", user.password)
                put("phone", user.phone)
            }
            val success = db.insertOrThrow("users", null, values) != -1L
            db.close()
            success
        } catch (e: Exception) {
            Log.e("DatabaseError", "Insert failed: ${e.message}")
            db.close()
            false
        }
    }

    fun updateUser(user: User): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("phone", user.phone)
        }
        val rowsAffected = db.update("users", values, "id = ?", arrayOf(user.id.toString()))
        db.close()
        return rowsAffected > 0
    }

    fun deleteUser(id: Int): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete("users", "id = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted > 0
    }

    fun deleteAllUsers() {
        val db = writableDatabase
        db.delete("users", null, null)
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS items")
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("ALTER TABLE users ADD COLUMN phone TEXT NOT NULL DEFAULT ''")
        onCreate(db)
    }

    fun getUserById(id: Int): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("email")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone"))
            )
            cursor.close()
            db.close()
            user
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun getAllUserPhones(): List<String> {
        val phoneList = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT phone FROM users", null)

        if (cursor.moveToFirst()) {
            do {
                phoneList.add(cursor.getString(cursor.getColumnIndexOrThrow("phone")))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return phoneList
    }

}
