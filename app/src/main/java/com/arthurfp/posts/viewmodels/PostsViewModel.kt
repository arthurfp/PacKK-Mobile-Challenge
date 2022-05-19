package com.arthurfp.posts.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.arthurfp.posts.data.Repository
import com.arthurfp.posts.models.Comment
import com.arthurfp.posts.models.Post
import com.arthurfp.posts.utils.NetworkResult
import com.arthurfp.posts.utils.NetworkUtils.hasInternetConnection
import com.arthurfp.posts.utils.handleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val postsResponse: MutableLiveData<NetworkResult<List<Post>>> = MutableLiveData()

    val commentsResponse: MutableLiveData<NetworkResult<List<Comment>>> = MutableLiveData()

    fun getPosts() = viewModelScope.launch {
        getPostsCall()
    }

    fun getComments(postId: Int) = viewModelScope.launch {
        getCommentsCall(postId)
    }

    fun getLocalComments(postId: Int): LiveData<List<Comment>> {

        // Important: Usually I would just get all the comments from API. But since JSONPlaceholder API does not support "real" PUT/POST,
        // as stated on their website: "Important: resource will not be really updated on the server but it will
        // be faked as if" (https://jsonplaceholder.typicode.com/guide/), I decided to save new comments locally. Therefore I need to
        // merge both sources before displaying them.

        return repository.local.readComments(postId).asLiveData()
    }

    fun insertComment(postId: Int, body: String) = viewModelScope.launch(Dispatchers.IO) {
        val comment = Comment(id= 0, body = body, postId = postId)
        repository.local.insertComment(comment)
    }

    private suspend fun getPostsCall(){
        if (hasInternetConnection(getApplication())) {

            try {
                val response = repository.remote.getPosts()
                postsResponse.value = response.handleResponse()
            } catch (e: Exception) {
                postsResponse.value = NetworkResult.Error("Posts not found!")
            }

        } else {
            postsResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getCommentsCall(postId: Int){

        if (hasInternetConnection(getApplication())) {

            try {
                val remoteResponse = repository.remote.getComments(postId)
                commentsResponse.value = remoteResponse.handleResponse()
            } catch (e: Exception) {
                commentsResponse.value = NetworkResult.Error("Posts not found!")
            }

        } else {
            commentsResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }
}