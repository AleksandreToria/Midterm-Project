package com.example.store.presentation.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.ItemLayoutBinding
import com.example.store.presentation.extension.loadImage
import com.example.store.presentation.model.product.Product

class HomeFragmentRecyclerAdapter :
    ListAdapter<Product, HomeFragmentRecyclerAdapter.ProductsViewHolder>(ProductsDiffUtil()) {

    inner class ProductsViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: Product

        fun bind() {
            model = currentList[adapterPosition]
            with(binding) {
                image.loadImage(model.image)
                title.text = model.title
                description.text = model.description
                price.text = model.price.toString()
                rating.text = model.rating.rate.toString()
                count.text = model.rating.count.toString()
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
        holder.bind()
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