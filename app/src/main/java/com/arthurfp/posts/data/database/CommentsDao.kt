package com.arthurfp.posts.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arthurfp.posts.models.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Query("SELECT * FROM comments_table WHERE postId =:postId ORDER BY id ASC")
    fun readComments(postId: Int): Flow<List<Comment>>

}