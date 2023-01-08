package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "other_payment_received_table")
data class OtherPaymentReceived(
    @PrimaryKey(autoGenerate = true)
    var otherPaymentId: Int = 1,
    var senderName: String? = null,
    val paymentMethod: String? = null,
    val amount: String? = null
)
