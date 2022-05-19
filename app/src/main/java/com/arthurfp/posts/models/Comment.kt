package com.arthurfp.posts.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

import com.arthurfp.posts.utils.Constants.Companion.TABLE_COMMENTS;

@Entity(tableName = TABLE_COMMENTS)
data class Comment(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int,
    @SerializedName("body")
    val body: String,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("name")
    val name: String = "Anonymous",
    @SerializedName("postId")
    val postId: Int
)