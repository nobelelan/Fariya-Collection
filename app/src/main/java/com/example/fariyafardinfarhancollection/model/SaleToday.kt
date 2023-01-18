package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_today_table")
data class SaleToday(
    @PrimaryKey(autoGenerate = false)
    var saleId: Int = 0,
    var date: String? = "",
    var retailSale: String? = "",
    var wholesale: String? = "",
    var otherPayment: String? = "",
    var spentToday: String? = "",
    var retailTotal: String? = "",
    var wholesaleTotal: String? = "",
    var otherPaymentTotal: String? = "",
    var spentTodayTotal: String? = "",
    var comment: String? = "",
    var retailAfterSpentMinus: String? = ""
)
