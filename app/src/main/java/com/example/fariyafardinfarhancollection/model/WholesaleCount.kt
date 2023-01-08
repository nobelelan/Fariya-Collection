package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wholesale_count_table")
data class WholesaleCount(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1,
    var name: String? = null,
    var quantity: String? = null,
    var price: String? = null,
    var total: String? = null
)
