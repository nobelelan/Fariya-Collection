package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spent_today_table")
data class SpentToday(
    @PrimaryKey(autoGenerate = false)
    var spentTodayId: Int = 0,
    var reason: String? = "",
    var amount: String? = ""
)
