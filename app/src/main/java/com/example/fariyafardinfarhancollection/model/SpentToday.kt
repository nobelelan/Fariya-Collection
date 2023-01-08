package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spent_today_table")
data class SpentToday(
    @PrimaryKey(autoGenerate = true)
    var spentTodayId: Int = 1,
    var reason: String? = null,
    var amount: String? = null
)
