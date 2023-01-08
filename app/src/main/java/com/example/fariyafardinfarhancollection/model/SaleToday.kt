package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_today_table")
data class SaleToday(
    @PrimaryKey(autoGenerate = true)
    var saleId: Int = 1,
    var date: String? = null,
    var retailSale: String? = null,
    var wholesale: String? = null,
    var wholesaleTotal: String? = null,
    var retailTotal: String? = null
)
