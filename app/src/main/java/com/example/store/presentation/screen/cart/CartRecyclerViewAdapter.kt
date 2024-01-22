package com.example.store.presentation.screen.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.CartItemLayoutBinding
import com.example.store.domain.local.model.CartEntity
import com.example.store.presentation.extension.loadImage

class CartRecyclerViewAdapter :
    ListAdapter<CartEntity, CartRecyclerViewAdapter.CartViewHolder>(CartDiffUtil()) {

    private var quantities = mutableMapOf<Int, Int>()

    var quantityChangeListener: OnQuantityChangeListener? = null

    fun getCurrentQuantities(): Map<Int, Int> = quantities

    var onDeleteListener: OnDeleteListener? = null

    inner class CartViewHolder(private val binding: CartItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(cartEntity: CartEntity) {
            with(binding) {
                image.loadImage(cartEntity.image)
                title.text = cartEntity.title
                price.text = "$${cartEntity.price}"

                val currentQuantity = quantities[cartEntity.id] ?: 1
                quantity.text = currentQuantity.toString()

                plusBtn.setOnClickListener {
                    val newQuantity = (quantities[cartEntity.id] ?: 0) + 1
                    quantities[cartEntity.id] = newQuantity
                    quantity.text = newQuantity.toString()
                    quantityChangeListener?.onQuantityChanged()
                }

                minusBtn.setOnClickListener {
                    val newQuantity = (quantities[cartEntity.id] ?: 2) - 1
                    if (newQuantity > 0) {
                        quantities[cartEntity.id] = newQuantity
                        quantity.text = newQuantity.toString()
                        quantityChangeListener?.onQuantityChanged()
                    }
                }

                binding.deleteBtn.setOnClickListener {
                    onDeleteListener?.onDelete(cartEntity)
                }
            }
        }
    }

    interface OnDeleteListener {
        fun onDelete(cartEntity: CartEntity)
    }

    interface OnQuantityChangeListener {
        fun onQuantityChanged()
    }

    class CartDiffUtil : DiffUtil.ItemCallback<CartEntity>() {
        override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartViewHolder(
        CartItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}