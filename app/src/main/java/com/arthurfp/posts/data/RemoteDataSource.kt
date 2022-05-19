package com.arthurfp.posts.data

import com.arthurfp.posts.models.Comment
import com.arthurfp.posts.models.Post
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val postsApiService: PostsApiService
) {

    suspend fun getPosts(): Response<List<Post>> {
        return postsApiService.getPosts()
    }

    suspend fun getComments(postId: Int): Response<List<Comment>> {
        return postsApiService.getComments(postId)
    }
}