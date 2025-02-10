package com.example.qualif.model

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val compatibleMinecraftVersion: String
){}
