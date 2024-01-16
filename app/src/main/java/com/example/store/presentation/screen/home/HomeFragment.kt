package com.example.store.presentation.screen.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.store.databinding.FragmentHomeBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.product.ProductEvent
import com.example.store.presentation.extension.showSnackBar
import com.example.store.presentation.state.product.ProductState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var productAdapter: HomeFragmentRecyclerAdapter

    override fun bind() {
        productAdapter = HomeFragmentRecyclerAdapter()
        with(binding.recyclerView) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
        viewModel.onEvent(ProductEvent.FetchProducts)
    }

    override fun bindViewActionListeners() {

    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productState.collect {
                    handleProductState(it)
                }
            }
        }
    }

    private fun handleProductState(state: ProductState) {
        binding.progressBar.visibility =
            if (state.isLoading) View.VISIBLE else View.GONE

        state.products?.let {
            productAdapter.submitList(it)
        }

        state.errorMessage?.let {
            binding.root.showSnackBar(it)
            viewModel.onEvent(ProductEvent.ResetErrorMessage)
        }
    }
}