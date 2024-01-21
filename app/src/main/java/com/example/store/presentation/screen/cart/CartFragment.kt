package com.example.store.presentation.screen.cart

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.databinding.FragmentCartBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.cart.CartEvent
import com.example.store.presentation.state.cart.CartState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment @Inject constructor() :
    BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private val viewModel: CartFragmentViewModel by viewModels()
    private val adapter = CartRecyclerViewAdapter()

    override fun bind() {
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
        }

        viewModel.onEvent(CartEvent.GetItems)
    }

    override fun bindViewActionListeners() {
        binding.apply {
            buy.setOnClickListener {
                viewModel.onEvent(CartEvent.RemoveAllItems)
            }
        }
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartState.collect {
                    handleCartState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    handleNavigationEvents(event)
                }
            }
        }
    }

    private fun handleCartState(state: CartState) {
        state.allItems?.let {
            adapter.submitList(it)
        }
    }

    private fun handleNavigationEvents(event: CartFragmentViewModel.CartUiEvent) {
        when (event) {
            CartFragmentViewModel.CartUiEvent.NavigateToHomeFragment -> {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToHomeFragment())
            }
        }
    }
}