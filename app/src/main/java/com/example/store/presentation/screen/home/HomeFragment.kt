package com.example.store.presentation.screen.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.databinding.FragmentHomeBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.product.StoreEvent
import com.example.store.presentation.extension.showSnackBar
import com.example.store.presentation.state.product.ProductState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private val productAdapter = HomeFragmentRecyclerAdapter()
    private val categoryAdapter = CategoryRecyclerAdapter()

    override fun bind() {
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.adapter = productAdapter
        }

        binding.apply {
            categoryRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            categoryRecyclerView.adapter = categoryAdapter
        }

        viewModel.onEvent(StoreEvent.FetchProducts)
        viewModel.onEvent(StoreEvent.FetchCategories)
    }

    override fun bindViewActionListeners() {
        productAdapter.setOnItemClickListener {
            viewModel.onEvent(StoreEvent.ItemClick(it.id))
        }

        categoryAdapter.setOnItemClickListener {
            viewModel.onEvent(StoreEvent.FetchProductsByCategory(it))
        }
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productState.collect {
                    handleProductState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    handleNavigationEvents(event = it)
                }
            }
        }
    }

    private fun handleProductState(state: ProductState) {
        binding.progressBar.visibility =
            if (state.isLoading) View.VISIBLE else View.GONE

        state.categories?.let {
            categoryAdapter.submitList(it)
        }

        state.products?.let {
            productAdapter.submitList(it)
        }

        state.categoryProducts?.let {
            productAdapter.submitList(it)
        }

        state.errorMessage?.let {
            binding.root.showSnackBar(it)
            viewModel.onEvent(StoreEvent.ResetErrorMessage)
        }
    }

    private fun handleNavigationEvents(event: HomeFragmentViewModel.HomeFragmentUiEvent) {
        when (event) {
            is HomeFragmentViewModel.HomeFragmentUiEvent.NavigateToProductInfo -> {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToProductInfoFragment(
                        id = event.id
                    )
                )
            }
        }
    }
}