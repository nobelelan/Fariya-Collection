package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_product_table")
data class StoreProduct (
    @PrimaryKey(autoGenerate = true)
    var storeProductId: Int = 1,
    var productName: String? = null,
    var quantityLeft: String? = null,
    var retailPrice: String? = null,
    var wholesalePrice: String? = null
)