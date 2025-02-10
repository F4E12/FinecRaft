package com.example.qualif.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.qualif.model.Item

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "item.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createItemTable = """
            CREATE TABLE IF NOT EXISTS item (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createItemTable)
    }

    fun getItems(): List<Item> {
        val items = arrayListOf<Item>()

        val db = readableDatabase
        val query = "SELECT * FROM item"

        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (cursor.count > 0) {
            do {
                val item = Item(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description"))
                )

                items.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return items;
    }

    fun insertItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
        }
        db.insert("item", null, values)
        db.close()
    }

    fun updateItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
        }
        db.update("item", values, "id = ?", arrayOf(item.id.toString()))
        db.close()
    }

    fun deleteItem(id: String) {
        val db = writableDatabase
        db.delete("item", "id = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllItem() {
        val db = writableDatabase
        db.delete("item", null, null)
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS item")
        onCreate(db)
    }
}