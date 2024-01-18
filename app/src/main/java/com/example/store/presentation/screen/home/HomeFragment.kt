package com.example.store.presentation.screen.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.store.databinding.FragmentHomeBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.product.ProductEvent
import com.example.store.presentation.extension.showSnackBar
import com.example.store.presentation.state.product.ProductState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private var adapter = HomeFragmentRecyclerAdapter()

    override fun bind() {
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.adapter = adapter
        }

        viewModel.onEvent(ProductEvent.FetchProducts)
    }

    override fun bindViewActionListeners() {
        adapter.setOnItemClickListener {
            viewModel.onEvent(ProductEvent.ItemClick(it.id))
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

        state.products?.let {
            adapter.submitList(it)
        }

        state.errorMessage?.let {
            binding.root.showSnackBar(it)
            viewModel.onEvent(ProductEvent.ResetErrorMessage)
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