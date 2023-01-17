package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_contact_table")
data class CustomerContact(
    @PrimaryKey(autoGenerate = false)
    var ccId: Int = 0,
    var name: String? = "",
    var number: String? = "",
    var address: String? = "",
    var due: Int? = null
)
