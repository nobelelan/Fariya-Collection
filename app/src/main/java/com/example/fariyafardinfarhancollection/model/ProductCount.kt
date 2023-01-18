package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_count_table")
data class ProductCount(
    @PrimaryKey(autoGenerate = false)
    var pcId: Int = 0,
    var name: String? = "",
    var quantity: String? = "",
    var price: String? = "",
    var total: String? = ""
)
