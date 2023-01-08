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
import com.example.fariyafardinfarhancollection.model.WholesaleCount
import com.example.fariyafardinfarhancollection.verifyProductCountDataFromUser

class SalesWholesaleCountAdapter :
    RecyclerView.Adapter<SalesWholesaleCountAdapter.SalesProductCountViewHolder>() {

    inner class SalesProductCountViewHolder(val binding: RvSalesProductCountBinding) :
        RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<WholesaleCount>() {
        override fun areItemsTheSame(oldItem: WholesaleCount, newItem: WholesaleCount): Boolean {
            return oldItem.wsId == newItem.wsId
        }

        override fun areContentsTheSame(oldItem: WholesaleCount, newItem: WholesaleCount): Boolean {
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
        val wholesaleCount = differ.currentList[position]

        holder.binding.txtSerialNo.text = " ${wholesaleCount.wsId} "

        holder.binding.edtProductName.setText(wholesaleCount.name)
        holder.binding.edtQuantity.setText(wholesaleCount.quantity)
        holder.binding.edtPrice.setText(wholesaleCount.price)
        holder.binding.txtTotal.text = wholesaleCount.total ?: "="

        holder.binding.txtTotal.setOnClickListener {
            val name = holder.binding.edtProductName.text.toString()
            val quantity = holder.binding.edtQuantity.text.toString()
            val price = holder.binding.edtPrice.text.toString()
            if (verifyProductCountDataFromUser(name, quantity, price)){
                val total = quantity.toInt() * price.toInt()
                updateItemListener?.updateProductCount(wholesaleCount.wsId, name, quantity, price, total.toString())
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