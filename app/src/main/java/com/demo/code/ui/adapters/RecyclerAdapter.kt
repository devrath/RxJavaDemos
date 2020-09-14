package com.demo.code.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.code.R
import com.demo.code.models.Post
import com.demo.code.ui.adapters.RecyclerAdapter.MyViewHolder
import java.util.*

class RecyclerAdapter : RecyclerView.Adapter<MyViewHolder>() {
    private var posts: MutableList<Post> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_list_item, null, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun setPosts(posts: MutableList<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    fun updatePost(post: Post) {
        posts[posts.indexOf(post)] = post
        notifyItemChanged(posts.indexOf(post))
    }

    fun getPosts(): List<Post> {
        return posts
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var title: TextView = itemView.findViewById(R.id.title)
        private var numComments: TextView = itemView.findViewById(R.id.num_comments)
        private var progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        fun bind(post: Post) {
            title.text = post.title

            if(post.comments == null){
                showProgressBar(true);
                numComments.text = ""
            }
            else{
                showProgressBar(false);
                numComments.text = post.comments.size.toString()
            }
        }

        private fun showProgressBar(showProgressBar: Boolean) {
            if (showProgressBar) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

    }

    companion object {
        private const val TAG = "RecyclerAdapter"
    }
}