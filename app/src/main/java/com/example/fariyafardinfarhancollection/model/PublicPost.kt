package com.example.fariyafardinfarhancollection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "public_post_table")
data class PublicPost(
    @PrimaryKey(autoGenerate = true)
    var publicPostId: Int = 1,
    var employeeName: String? = null,
    var dateAndTime: String? = null,
    var post: String? = null
)
