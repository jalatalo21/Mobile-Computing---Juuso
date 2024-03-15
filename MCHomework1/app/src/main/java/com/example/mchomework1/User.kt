package com.example.mchomework1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "profile_image") val image: String,
    @ColumnInfo(name = "messages") val messageList: String,
)