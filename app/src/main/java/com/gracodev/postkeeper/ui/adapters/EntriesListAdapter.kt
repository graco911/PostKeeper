package com.gracodev.postkeeper.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gracodev.postkeeper.Utils.toFormattedDate
import com.gracodev.postkeeper.Utils.truncate
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.databinding.EntryListItemBinding

class EntriesListAdapter(
    private val blogPostList: MutableList<BlogPostData> = mutableListOf(),
    private val onItemClick: (BlogPostData) -> Unit
) : RecyclerView.Adapter<EntriesListAdapter.ViewHolder>() {

    fun submitAll(newBlogPostList: MutableList<BlogPostData>) {
        blogPostList.clear()
        blogPostList.addAll(newBlogPostList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            EntryListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(blogPostList[position])
    }

    override fun getItemCount() = blogPostList.size

    inner class ViewHolder(val binding: EntryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blogPost: BlogPostData) {
            binding.apply {
                tvAuthor.text = blogPost.author
                tvDate.text = blogPost.timeStamp.toFormattedDate()
                tvText.text = blogPost.body.truncate(70)
                tvTitle.text = blogPost.title
            }
            binding.root.setOnClickListener { onItemClick.invoke(blogPost) }
        }
    }
}