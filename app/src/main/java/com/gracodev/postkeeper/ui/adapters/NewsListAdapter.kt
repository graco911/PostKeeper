package com.gracodev.postkeeper.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gracodev.postkeeper.Utils.formatToCustomDate
import com.gracodev.postkeeper.data.models.Article
import com.gracodev.postkeeper.databinding.NewListItemBinding

class NewsListAdapter(
    private val newsList: MutableList<Article> = mutableListOf(),
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    fun submitAll(newNewsList: MutableList<Article>) {
        newsList.clear()
        newsList.addAll(newNewsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(NewListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: NewsListAdapter.ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size

    inner class ViewHolder(val binding: NewListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                tvSourceName.text = article.source.name
                tvDescription.text = article.title
                tvDate.text = article.publishedAt.formatToCustomDate()
            }
            binding.root.setOnClickListener { onItemClick.invoke(article) }
        }
    }
}