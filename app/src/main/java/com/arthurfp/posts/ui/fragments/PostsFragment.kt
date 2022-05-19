package com.arthurfp.posts.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthurfp.posts.databinding.PostsFragmentBinding
import com.arthurfp.posts.models.Comment
import com.arthurfp.posts.models.Post
import com.arthurfp.posts.ui.adapters.CommentsAdapter
import com.arthurfp.posts.ui.adapters.PostsAdapter
import com.arthurfp.posts.utils.NetworkResult
import com.arthurfp.posts.utils.hideKeyboard
import com.arthurfp.posts.utils.observeOnce
import com.arthurfp.posts.viewmodels.PostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private var _binding: PostsFragmentBinding? = null
    private val binding get() = _binding!!

    private val mPostsAdapter: PostsAdapter by lazy { PostsAdapter(::handleCommentsResponse, ::postComment) }
    private val mCommentsAdapter: CommentsAdapter by lazy { CommentsAdapter() }

    private lateinit var postsViewModel: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postsViewModel = ViewModelProvider(requireActivity())[PostsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PostsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            handlePostsResponse()
        }

        return binding.root
    }

    private fun handlePostsResponse() {
        postsViewModel.getPosts()
        postsViewModel.postsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT)
                }
                is NetworkResult.Success -> {
                    response.data?.let {
                        setupRecyclerView(it)
                    }
                }
            }
        })
    }

    private fun handleCommentsResponse(postId: Int, holder: PostsAdapter.PostsViewHolder) {
        postsViewModel.getComments(postId)
        postsViewModel.commentsResponse.observeOnce(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT)
                }
                is NetworkResult.Success -> {
                    response.data?.let {
                        setupCommentsRecyclerView(holder, it)
                        handleLocalCommentsResponse(postId, holder)
                    }
                }
            }
        })
    }

    private fun handleLocalCommentsResponse(postId: Int, holder: PostsAdapter.PostsViewHolder) {
        postsViewModel.getLocalComments(postId)
        postsViewModel.getLocalComments(postId).observeOnce(viewLifecycleOwner, { response ->
            if (response.isNotEmpty()) {
                // merge local comments with remote comments
                setupCommentsRecyclerView(holder, mCommentsAdapter.commentsList + response)
            }
        })
    }

    private fun postComment(postId: Int, body: String, holder: PostsAdapter.PostsViewHolder) {
        postsViewModel.insertComment(postId, body)

        // reload comments after inserting a new one
        handleCommentsResponse(postId, holder)

        // clear input field
        holder.binding.inputComment.text.clear()

        hideKeyboard()
    }


    private fun setupRecyclerView(data: List<Post>) {
        mPostsAdapter.postsList = data
        binding.recyclerViewPosts.adapter = mPostsAdapter
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupCommentsRecyclerView(holder: PostsAdapter.PostsViewHolder, data: List<Comment>) {
        mCommentsAdapter.commentsList = data
        holder.binding.recyclerViewComments.adapter = mCommentsAdapter
        holder.binding.recyclerViewComments.layoutManager = LinearLayoutManager(holder.binding.recyclerViewComments.context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Avoid memory leaks
        _binding = null
    }

}