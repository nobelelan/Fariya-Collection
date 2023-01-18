package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "public_post_table")
data class PublicPost(
    @PrimaryKey(autoGenerate = false)
    var publicPostId: Int = 0,
    var employeeName: String? = "",
    var dateAndTime: String? = "",
    var post: String? = ""
)
