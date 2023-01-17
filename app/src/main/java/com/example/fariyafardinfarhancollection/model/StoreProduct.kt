package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_product_table")
data class StoreProduct (
    @PrimaryKey(autoGenerate = false)
    var storeProductId: Int = 0,
    var productName: String? = "",
    var quantityLeft: String? = "",
    var retailPrice: String? = "",
    var wholesalePrice: String? = ""
)