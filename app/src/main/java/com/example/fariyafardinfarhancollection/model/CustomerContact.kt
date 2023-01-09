package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_contact_table")
data class CustomerContact(
    @PrimaryKey(autoGenerate = true)
    var ccId: Int = 1,
    var name: String? = null,
    var number: String? = null,
    var address: String? = null,
    var due: Int? = null
)
