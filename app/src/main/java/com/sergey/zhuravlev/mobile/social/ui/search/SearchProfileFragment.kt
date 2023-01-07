package com.sergey.zhuravlev.mobile.social.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergey.zhuravlev.mobile.social.R
import com.sergey.zhuravlev.mobile.social.databinding.FragmentSearchProfileBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchProfileFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val vm by viewModel<SearchProfileViewModel>()

    val binding = FragmentSearchProfileBinding.inflate(inflater, container, false)
    val root: View = binding.root

    // Menu setup:

    val menuHost: MenuHost = requireActivity()

    menuHost.addMenuProvider(object : MenuProvider {

      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_appbar_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Start typing..."
        searchView.setOnQueryTextListener(
          object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
              vm.setQuery(query)
              return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
              return false
            }
          }
        )
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    // Views binding:

    val constraintLayoutCardProfile = binding.constraintLayoutCardProfile
    val profileSearchRecyclerView = binding.profileSearchRecyclerView
    val searchCountTextView = binding.searchCountTextView

    val adapter = SearchedProfileAdapter(requireContext())

    vm.profiles.observe(viewLifecycleOwner) { pagingData ->
      lifecycleScope.launch {
        adapter.submitData(pagingData)
      }
    }

    adapter.addLoadStateListener { loadState ->
      when (loadState.refresh) {
        is LoadState.Loading -> {
        }
        is LoadState.NotLoading -> {
          constraintLayoutCardProfile.visibility = if (adapter.itemCount > 0)
            View.VISIBLE
          else
            View.GONE
        }
        is LoadState.Error -> {}
      }
      //allCountTextView.text = "0 Subscribers | ${adapter.itemCount} Friends"
      searchCountTextView.text = adapter.pagingDataCount.toString()
    }

    profileSearchRecyclerView.layoutManager = LinearLayoutManager(
      requireContext(),
      LinearLayoutManager.VERTICAL,
      false
    )
    profileSearchRecyclerView.adapter = adapter

    return root
  }

}