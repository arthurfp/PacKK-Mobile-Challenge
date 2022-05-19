package com.arthurfp.posts.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arthurfp.posts.databinding.CommentItemLayoutBinding
import com.arthurfp.posts.models.Comment

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    var commentsList = emptyList<Comment>()

    class CommentsViewHolder(val binding: CommentItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            CommentItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.binding.titleComment.text = commentsList[position].name
        holder.binding.bodyComment.text = commentsList[position].body
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

}