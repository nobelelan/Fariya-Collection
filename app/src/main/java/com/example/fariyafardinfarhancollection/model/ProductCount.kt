package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_count_table")
data class ProductCount(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1,
    var name: String? = null,
    var quantity: Int? = null,
    var price: Int? = null,
    var total: Int? = null
)
