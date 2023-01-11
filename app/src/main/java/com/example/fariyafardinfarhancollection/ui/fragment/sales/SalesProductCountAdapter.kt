package com.example.fariyafardinfarhancollection.ui.fragment.sales

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.databinding.RvSalesProductCountBinding
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.verifyProductCountDataFromUser

class SalesProductCountAdapter :
    RecyclerView.Adapter<SalesProductCountAdapter.SalesProductCountViewHolder>() {

    inner class SalesProductCountViewHolder(val binding: RvSalesProductCountBinding) :
        RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<ProductCount>() {
        override fun areItemsTheSame(oldItem: ProductCount, newItem: ProductCount): Boolean {
            return oldItem.pcId == newItem.pcId
        }

        override fun areContentsTheSame(oldItem: ProductCount, newItem: ProductCount): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesProductCountViewHolder {
        return SalesProductCountViewHolder(
            RvSalesProductCountBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SalesProductCountViewHolder, position: Int) {
        val productCount = differ.currentList[position]

        holder.binding.edtProductName.setText(productCount.name)
        holder.binding.edtQuantity.setText(productCount.quantity)
        holder.binding.edtPrice.setText(productCount.price)
        holder.binding.txtTotal.text = productCount.total ?: "="

        holder.binding.txtTotal.setOnClickListener {
            val name = holder.binding.edtProductName.text.toString()
            val quantity = holder.binding.edtQuantity.text.toString()
            val price = holder.binding.edtPrice.text.toString()
            if (verifyProductCountDataFromUser(name, quantity, price)){
                val total = quantity.toInt() * price.toInt()
                updateItemListener?.updateProductCount(productCount.pcId, name, quantity, price, total.toString())
            }else{
                Toast.makeText(holder.itemView.context, "Fill out all the fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var updateItemListener: UpdateItemListener? = null

    interface UpdateItemListener {
        fun updateProductCount(
            id: Int,
            name: String,
            quantity: String,
            price: String,
            total: String
        )
    }

    fun setUpdateItemListener(listener: UpdateItemListener) {
        updateItemListener = listener
    }
}