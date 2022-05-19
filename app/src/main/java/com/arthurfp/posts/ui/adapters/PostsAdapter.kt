package com.arthurfp.posts.ui.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arthurfp.posts.R
import com.arthurfp.posts.databinding.PostItemLayoutBinding
import com.arthurfp.posts.models.Post
import com.arthurfp.posts.utils.toggleVisibility

class PostsAdapter(
    private val loadCommentsCallback: (Int, PostsViewHolder) -> Unit,
    private val postCommentCallback: (Int, String, PostsViewHolder) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    var postsList = emptyList<Post>()

    // Override OnItemTouchListener object in order to have proper nested scroll (otherwise, only the parent recyclerview will scroll)
    private val mScrollTouchListener by lazy { object :RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            when (e.action) {
                MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        }

    }}

    class PostsViewHolder(val binding: PostItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(
            PostItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.binding.titlePost.text = postsList[position].title
        holder.binding.bodyPost.text = postsList[position].body

        holder.binding.layoutCommentButton.setOnClickListener { loadComments(postsList[position].id, holder) }

        holder.binding.iconSubmit.setOnClickListener { postComment(postsList[position].id, holder) }
    }

    private fun loadComments(postId: Int, holder: PostsViewHolder) {
        val visible = holder.binding.cardViewComments.toggleVisibility()
        if (visible) {
            loadCommentsCallback(postId, holder)
            holder.binding.recyclerViewComments.addOnItemTouchListener(mScrollTouchListener);
        }
    }

    private fun postComment(postId: Int, holder: PostsViewHolder) {
        val strComment = holder.binding.inputComment.text.toString()

        if (strComment.isBlank()) {
            holder.binding.inputComment.context?.let{
                Toast.makeText(
                    it,
                    it.getString(R.string.empty_comment),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            postCommentCallback(postId, strComment, holder)
        }

    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}