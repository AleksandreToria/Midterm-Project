package com.example.store.presentation.screen.product_info

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.store.databinding.FragmentProductInfoBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.product.ProductEvent
import com.example.store.presentation.extension.loadImage
import com.example.store.presentation.extension.showSnackBar
import com.example.store.presentation.state.product.ProductState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductInfoFragment :
    BaseFragment<FragmentProductInfoBinding>(FragmentProductInfoBinding::inflate) {

    private val viewModel: ProductInfoViewModel by viewModels()

    override fun bind() {
        arguments?.let {
            val safeArgs = ProductInfoFragmentArgs.fromBundle(it)
            viewModel.onEvent(ProductEvent.FetchProductInfo(safeArgs.id))
        }
    }

    override fun bindViewActionListeners() {

    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productInfoState.collect {
                    handleProductState(it)

                    with(binding) {
                        image.loadImage(it.productInfo?.image)
                        category.text = it.productInfo?.category
                        title.text = it.productInfo?.title
                        price.text = it.productInfo?.price.toString()
                        rating.text = it.productInfo?.rating?.rate.toString()
                        count.text = it.productInfo?.rating?.count.toString()
                        description.text = it.productInfo?.description
                    }
                }
            }
        }
    }

    private fun handleProductState(state: ProductState) {
        binding.progressBar.visibility =
            if (state.isLoading) View.VISIBLE else View.GONE

        state.productInfo?.let {

        }

        state.errorMessage?.let {
            binding.root.showSnackBar(it)
            viewModel.onEvent(ProductEvent.ResetErrorMessage)
        }
    }
}