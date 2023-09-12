package com.gracodev.postkeeper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.gracodev.postkeeper.R
import com.gracodev.postkeeper.Utils.isInternetAvailable
import com.gracodev.postkeeper.Utils.toJson
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.databinding.FragmentSearchEntriesBinding
import com.gracodev.postkeeper.ui.activities.MainActivity
import com.gracodev.postkeeper.ui.adapters.EntriesListAdapter
import com.gracodev.postkeeper.ui.states.UIStates
import com.gracodev.postkeeper.ui.viewmodels.BlogRoomViewModel
import com.gracodev.postkeeper.ui.viewmodels.BlogViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchEntriesFragment : BaseFragment() {

    override var TAG: String = this.javaClass.name

    private val viewModel: BlogViewModel by activityViewModel()
    private val viewModelRoom: BlogRoomViewModel by activityViewModel()

    private val binding: FragmentSearchEntriesBinding by lazy {
        FragmentSearchEntriesBinding.inflate(layoutInflater)
    }

    private val entriesListAdapter: EntriesListAdapter by lazy {
        EntriesListAdapter() {
            handleTap(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpFilterSearch()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).revealFAB()
    }

    private fun setUpRecyclerView() {
        binding.apply {
            recyclerViewSearchEntries.adapter = entriesListAdapter
        }
    }

    private fun handleTap(blogPost: BlogPostData) {
        findNavController().navigate(
            R.id.action_searchEntriesFragment_to_entryDetailFragment, bundleOf(
                EntriesList.SEND_BLOG_TO_DETAIL_PAGE to blogPost.toJson()
            )
        )
    }

    private fun setUpFilterSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterEntries(newText.orEmpty(), getSelectedChips())
                return true
            }
        })
    }

    private fun filterEntries(searchText: String, selectedChips: Set<Int>) {

        val allEntries = if (requireContext().isInternetAvailable()) {
            (viewModel.getPostsResultLiveData.value as UIStates.Success).value ?: emptyList()
        } else {
            (viewModelRoom.getPostsResultLiveData.value as UIStates.Success).value ?: emptyList()
        }

        val filteredEntries = mutableListOf<BlogPostData>()

        for (entry in allEntries) {

            val shouldSearchInAllProperties = selectedChips.isEmpty()

            val propertyToFilter: String = when {
                shouldSearchInAllProperties -> "${entry.title} ${entry.author} ${entry.body}"
                R.id.chipOption1 in selectedChips -> entry.title
                R.id.chipOption2 in selectedChips -> entry.author
                R.id.chipOption3 in selectedChips -> entry.body
                else -> ""
            }

            // Aplica el filtrado según la propiedad seleccionada
            val matchesSearchText = propertyToFilter.contains(searchText, ignoreCase = true)
            if (matchesSearchText) {
                filteredEntries.add(entry)
            }
        }

        entriesListAdapter.submitAll(filteredEntries)
    }

    private fun getSelectedChips(): Set<Int> {
        val selectedChips = mutableSetOf<Int>()
        val chipGroup = binding.chipGroup

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedChips.add(chip.id)
            }
        }

        return selectedChips
    }
}