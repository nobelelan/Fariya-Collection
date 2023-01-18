package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wholesale_count_table")
data class WholesaleCount(
    @PrimaryKey(autoGenerate = false)
    var wsId: Int = 0,
    var name: String? = "",
    var quantity: String? = "",
    var price: String? = "",
    var total: String? = ""
)
