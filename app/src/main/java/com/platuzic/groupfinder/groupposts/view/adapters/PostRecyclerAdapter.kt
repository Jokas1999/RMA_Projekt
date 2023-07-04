package com.platuzic.groupfinder.groupposts.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.platuzic.groupfinder.GlideImageLoader
import com.platuzic.groupfinder.R
import com.platuzic.groupfinder.database.models.Post
import com.platuzic.groupfinder.databinding.ItemPostBinding

class PostRecyclerAdapter(
    private val posts: List<Post>
): RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            binding = ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(private val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(post: Post){
            binding.post = post
            if(post.text.isNullOrEmpty()){
                Glide.with(binding.ItemPostImagePost.context).load(post.image).error(R.drawable.ic_launcher_background).into(binding.ItemPostImagePost)
                binding.ItemPostTextPost.visibility = View.GONE
                binding.ItemPostImagePost.visibility = View.VISIBLE
            } else {
                binding.ItemPostImagePost.visibility = View.GONE
                binding.ItemPostTextPost.visibility = View.VISIBLE
            }
        }
    }
}