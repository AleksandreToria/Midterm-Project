package com.example.store.presentation.screen.cart

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.databinding.FragmentCartBinding
import com.example.store.domain.local.model.CartEntity
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

            adapter.quantityChangeListener = object :
                CartRecyclerViewAdapter.OnQuantityChangeListener {
                override fun onQuantityChanged() {
                    viewModel.onEvent(CartEvent.CalculateTotalPrice)
                }
            }

            adapter.quantityChangeListener = object :
                CartRecyclerViewAdapter.OnQuantityChangeListener {
                override fun onQuantityChanged() {
                    calculateAndUpdateTotalPrice()
                }
            }

            adapter.onDeleteListener = object : CartRecyclerViewAdapter.OnDeleteListener {
                override fun onDelete(cartEntity: CartEntity) {
                    viewModel.deleteItem(cartEntity)
                }
            }
        }

        viewModel.onEvent(CartEvent.GetItems)
    }

    override fun bindViewActionListeners() {
        binding.apply {
            buy.setOnClickListener {
                viewModel.onEvent(CartEvent.RemoveAllItems)
            }
            backBtn.setOnClickListener {
                viewModel.onEvent(CartEvent.NavigateHome)
            }
        }
    }

    @SuppressLint("SetTextI18n")
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalPrice.collect { totalPrice ->
                    binding.totalPrice.text = "$${String.format("%.2f", totalPrice)}"
                }
            }
        }
    }

    private fun handleCartState(state: CartState) {
        state.allItems?.let { items ->
            adapter.submitList(items) {
                calculateAndUpdateTotalPrice()
            }
        }
    }

    private fun handleNavigationEvents(event: CartFragmentViewModel.CartUiEvent) {
        when (event) {
            CartFragmentViewModel.CartUiEvent.NavigateToHomeFragment -> {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToHomeFragment())
            }
        }
    }

    private fun calculateAndUpdateTotalPrice() {
        val items = adapter.currentList
        var totalPrice = 0.0
        val quantities = adapter.getCurrentQuantities()
        items.forEach {
            val quantity = quantities[it.id] ?: 1
            totalPrice += it.price * quantity
        }
        viewModel.setTotalPrice(totalPrice)
    }
}