package com.example.qualif.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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
                password TEXT NOT NULL
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
                    cursor.getString(cursor.getColumnIndexOrThrow("password"))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }

    fun insertUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
        }
        db.insert("users", null, values)
        db.close()
    }

    fun updateUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
        }
        db.update("users", values, "id = ?", arrayOf(user.id.toString()))
        db.close()
    }

    fun deleteUser(id: String) {
        val db = writableDatabase
        db.delete("users", "id = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllUsers() {
        val db = writableDatabase
        db.delete("users", null, null)
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS items")
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
}
