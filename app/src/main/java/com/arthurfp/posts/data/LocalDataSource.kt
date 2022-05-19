package com.arthurfp.posts.data

import com.arthurfp.posts.data.database.CommentsDao
import com.arthurfp.posts.models.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val commentsDao: CommentsDao
) {

    suspend fun insertComment(comment: Comment) {
        commentsDao.insertComment(comment)
    }
    fun readComments(postId: Int): Flow<List<Comment>> {
        return commentsDao.readComments(postId)
    }

}