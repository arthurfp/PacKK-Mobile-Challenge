package com.arthurfp.posts.data

import com.arthurfp.posts.models.Comment
import com.arthurfp.posts.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsApiService {

    @GET("/posts")
    suspend fun getPosts(
    ) : Response<List<Post>>

    @GET("/posts/{postId}/comments")
    suspend fun getComments(
        @Path("postId") postId: Int
    ) : Response<List<Comment>>
}