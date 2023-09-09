package com.gracodev.postkeeper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gracodev.postkeeper.Utils.fromJson
import com.gracodev.postkeeper.Utils.toFormattedDate
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.databinding.FragmentEntryDetailBinding
import com.gracodev.postkeeper.ui.fragments.EntriesList.Companion.SEND_BLOG_TO_DETAIL_PAGE

class EntryDetailFragment : BaseFragment() {

    override var TAG: String = this.javaClass.name

    private val binding: FragmentEntryDetailBinding by lazy {
        FragmentEntryDetailBinding.inflate(layoutInflater)
    }

    private var blogData: BlogPostData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractBundleInfo()
        binding.blogData = blogData
        binding.tvDate.text = blogData?.timeStamp!!.toFormattedDate()
    }

    private fun extractBundleInfo() {
        arguments?.getString(SEND_BLOG_TO_DETAIL_PAGE)?.let {
            blogData = it.fromJson<BlogPostData>()
        }
    }
}