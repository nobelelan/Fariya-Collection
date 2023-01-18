package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "other_payment_received_table")
data class OtherPaymentReceived(
    @PrimaryKey(autoGenerate = false)
    var otherPaymentId: Int = 0,
    var senderName: String? = "",
    val paymentMethod: String? = "",
    val amount: String? = ""
)
