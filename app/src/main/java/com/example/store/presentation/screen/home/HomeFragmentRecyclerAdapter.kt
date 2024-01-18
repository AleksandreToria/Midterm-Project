package com.example.store.presentation.screen.home

import android.annotation.SuppressLint
import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.ItemLayoutBinding
import com.example.store.domain.model.store.GetProducts
import com.example.store.presentation.extension.loadImage
import com.example.store.presentation.model.product.Product

class HomeFragmentRecyclerAdapter :
    ListAdapter<Product, HomeFragmentRecyclerAdapter.ProductsViewHolder>(ProductsDiffUtil()) {

    private var onItemClick: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        this.onItemClick = listener
    }

    inner class ProductsViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            with(binding) {
                image.loadImage(product.image)
                title.text = product.title
                description.text = product.description
                price.text = "$${product.price}"
                rating.text = "Rating: ${product.rating.rate}"
                count.text = "Count: ${product.rating.count}"

                root.setOnClickListener {
                    onItemClick?.invoke(product)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductsViewHolder(
        ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductsDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}