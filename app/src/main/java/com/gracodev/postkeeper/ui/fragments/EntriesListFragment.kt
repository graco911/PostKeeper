package com.gracodev.postkeeper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gracodev.postkeeper.R
import com.gracodev.postkeeper.Utils.isInternetAvailable
import com.gracodev.postkeeper.Utils.snackbarError
import com.gracodev.postkeeper.Utils.toJson
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.databinding.FragmentEntriesListBinding
import com.gracodev.postkeeper.ui.adapters.EntriesListAdapter
import com.gracodev.postkeeper.ui.states.UIStates
import com.gracodev.postkeeper.ui.viewmodels.BlogRoomViewModel
import com.gracodev.postkeeper.ui.viewmodels.BlogViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class EntriesList : BaseFragment() {

    override var TAG: String = this.javaClass.name

    private val viewModel: BlogViewModel by activityViewModel()
    private val viewModelRoom: BlogRoomViewModel by activityViewModel()

    private val binding: FragmentEntriesListBinding by lazy {
        FragmentEntriesListBinding.inflate(layoutInflater)
    }

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeRefreshLayout
    }

    private val entriesListAdapter: EntriesListAdapter by lazy {
        EntriesListAdapter() {
            handleTap(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateDataSource()
        setUpOnClickListeners()
        setUpRecyclerView()
        setUpObservable()
        setUpSwipeToRefresh()
    }

    private fun setUpRecyclerView() {
        binding.apply {
            recylerViewEntriesList.adapter = entriesListAdapter
        }
    }

    private fun validateDataSource() {
        if (requireContext().isInternetAvailable())
            viewModel.getBlogPosts()
        else
            viewModelRoom.getBlogPosts()
    }

    private fun setUpOnClickListeners() {
    }

    private fun setUpObservable() {
        viewModel.getPostsResultLiveData.observe(viewLifecycleOwner) { uiState ->
            validateDataReponse(uiState)
        }
        viewModelRoom.getPostsResultLiveData.observe(viewLifecycleOwner) { uiState ->
            validateDataReponse(uiState)
        }
    }

    private fun validateDataReponse(uiState: UIStates<List<BlogPostData>>?) {
        when (uiState) {
            is UIStates.Error -> handleError(uiState)
            UIStates.Loading -> dialog.show(childFragmentManager, TAG)
            is UIStates.Success -> handleSuccess(uiState)
            else -> {}
        }
    }

    private fun setUpSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            validateDataSource()
        }
    }

    private fun handleSuccess(success: UIStates.Success<List<BlogPostData>>) {
        dialog.dismiss()
        if (requireContext().isInternetAvailable()) {
            viewModelRoom.updateBlogDB(success.value as MutableList<BlogPostData>)
        }

        entriesListAdapter.submitAll(success.value as MutableList<BlogPostData>)
        swipeRefreshLayout.isRefreshing = false
    }

    private fun handleError(error: UIStates.Error) {
        dialog.dismiss()
        swipeRefreshLayout.isRefreshing = false
        requireView().snackbarError(error.message)
    }

    private fun handleTap(blogPost: BlogPostData) {
        findNavController().navigate(
            R.id.action_navigation_list_to_entryDetailFragment, bundleOf(
                SEND_BLOG_TO_DETAIL_PAGE to blogPost.toJson()
            )
        )
    }

    companion object {
        const val SEND_BLOG_TO_DETAIL_PAGE = "send_blog_to_detail_page"
    }
}