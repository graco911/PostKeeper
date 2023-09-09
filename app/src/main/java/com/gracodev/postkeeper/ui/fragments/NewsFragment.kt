package com.gracodev.postkeeper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gracodev.postkeeper.Utils.openURLWithBrowser
import com.gracodev.postkeeper.Utils.snackbarError
import com.gracodev.postkeeper.data.models.Article
import com.gracodev.postkeeper.data.models.NewsResponseData
import com.gracodev.postkeeper.databinding.FragmentNewsBinding
import com.gracodev.postkeeper.ui.adapters.NewsListAdapter
import com.gracodev.postkeeper.ui.states.UIStates
import com.gracodev.postkeeper.ui.viewmodels.NewsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class NewsFragment : BaseFragment() {

    override var TAG: String = this.javaClass.name

    private val viewModel: NewsViewModel by activityViewModel()

    private val binding: FragmentNewsBinding by lazy {
        FragmentNewsBinding.inflate(layoutInflater)
    }

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeNewsRefreshLayout
    }

    private val newsListAdapter: NewsListAdapter by lazy {
        NewsListAdapter() {
            handleTap(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpNewsRequest()
        setUpRecyclerView()
        setUpObservable()
        setUpSwipeToRefresh()
    }

    private fun setUpRecyclerView() {
        binding.apply {
            recylerViewNewsList.adapter = newsListAdapter
        }
    }

    private fun setUpObservable() {
        viewModel.newsResultLiveData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UIStates.Error -> handleError(uiState)
                UIStates.Loading -> dialog.show(childFragmentManager, TAG)
                is UIStates.Success -> handleSuccess(uiState)
                else -> {}
            }
        }
    }

    private fun setUpSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            setUpNewsRequest()
        }
    }

    private fun setUpNewsRequest() {
        viewModel.getNews(
            "mx",
            "technology",
            "a76c92f4f36f483b8e8d629436500677"
        )
    }

    private fun handleSuccess(success: UIStates.Success<NewsResponseData>) {
        dialog.dismiss()
        newsListAdapter.submitAll((success.value as NewsResponseData).articles.toMutableList())
        swipeRefreshLayout.isRefreshing = false
    }

    private fun handleError(error: UIStates.Error) {
        dialog.dismiss()
        swipeRefreshLayout.isRefreshing = false
        requireView().snackbarError(error.message)
    }

    private fun handleTap(article: Article) {
        requireContext().openURLWithBrowser(article.url)
    }
}