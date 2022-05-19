package com.arthurfp.posts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arthurfp.posts.models.Comment

@Database(
    entities = [Comment::class],
    version = 1, // Schema version
    exportSchema = false
)
abstract class CommentsDatabase: RoomDatabase() {

    abstract fun commentsDao(): CommentsDao
}