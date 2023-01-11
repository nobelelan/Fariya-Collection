package com.example.fariyafardinfarhancollection.ui.fragment.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.databinding.RvProductStoreBinding
import com.example.fariyafardinfarhancollection.model.CustomerContact
import com.example.fariyafardinfarhancollection.model.StoreProduct

class StoreProductAdapter: RecyclerView.Adapter<StoreProductAdapter.StoreProductViewHolder>() {

    inner class StoreProductViewHolder(val binding: RvProductStoreBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<StoreProduct>(){
        override fun areItemsTheSame(oldItem: StoreProduct, newItem: StoreProduct): Boolean {
            return oldItem.storeProductId == newItem.storeProductId
        }

        override fun areContentsTheSame(oldItem: StoreProduct, newItem: StoreProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreProductViewHolder {
        return StoreProductViewHolder(RvProductStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoreProductViewHolder, position: Int) {
        val storeProduct = differ.currentList[position]

        holder.binding.apply {
            txtProductName.text = storeProduct.productName
            txtAvailable.text = storeProduct.quantityLeft
            txtRetailPrice.text = storeProduct.retailPrice
            txtWholesalePrice.text = storeProduct.wholesalePrice
        }
        holder.binding.imgEdit.setOnClickListener {
            onEditItemClickListener?.let { it(storeProduct) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onEditItemClickListener: ((StoreProduct)->Unit) ?= null
    fun setOnEditClickListener(listener: (StoreProduct)->Unit){
        onEditItemClickListener = listener
    }
}