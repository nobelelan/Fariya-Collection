package com.example.fariyafardinfarhancollection.ui.fragment.sales

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.databinding.RvSalesProductCountBinding
import com.example.fariyafardinfarhancollection.model.ProductCount

class SalesProductCountAdapter: RecyclerView.Adapter<SalesProductCountAdapter.SalesProductCountViewHolder>() {

    inner class SalesProductCountViewHolder(val binding: RvSalesProductCountBinding): RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<ProductCount>(){
        override fun areItemsTheSame(oldItem: ProductCount, newItem: ProductCount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductCount, newItem: ProductCount): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesProductCountViewHolder {
        return SalesProductCountViewHolder(RvSalesProductCountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SalesProductCountViewHolder, position: Int) {
        val productCount = differ.currentList[position]

        holder.binding.apply {
//            edtProductName.setSelection(2)
//            edtQuantity.setSelection(2)
//            edtPrice.setSelection(2)
            txtSerialNo.text = " ${productCount.id} "
//            edtProductName.hint = productCount.name.toString()
//            edtQuantity.hint = productCount.quantity.toString()
//            edtPrice.hint = productCount.price.toString()
//            txtTotal.text = productCount.total.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}