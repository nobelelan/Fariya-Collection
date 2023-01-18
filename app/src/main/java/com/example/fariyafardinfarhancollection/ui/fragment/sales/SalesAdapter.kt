package com.example.fariyafardinfarhancollection.ui.fragment.sales

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.databinding.RvSalesItemBinding
import com.example.fariyafardinfarhancollection.model.SaleToday

class SalesAdapter: RecyclerView.Adapter<SalesAdapter.SalesViewHolder>() {

    inner class SalesViewHolder(val binding: RvSalesItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<SaleToday>(){
        override fun areItemsTheSame(oldItem: SaleToday, newItem: SaleToday): Boolean {
            return oldItem.saleId == newItem.saleId
        }

        override fun areContentsTheSame(oldItem: SaleToday, newItem: SaleToday): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        return SalesViewHolder(RvSalesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        val saleToday = differ.currentList[position]

        holder.binding.apply {
            txtCurrentDate.text = saleToday.date
            txtProductCountSale.text = saleToday.retailSale
            txtRetailTotal.text = saleToday.retailTotal
            txtWholesaleSale.text = saleToday.wholesale
            txtWholesaleTotal.text = saleToday.wholesaleTotal
            txtOtherPaymentAmount.text = saleToday.otherPayment
            txtOtherPaymentTotal.text = saleToday.otherPaymentTotal
            txtSpentTodayAmount.text = saleToday.spentToday
            txtSpentTodayTotal.text = saleToday.spentTodayTotal
            txtComment.text = saleToday.comment
            txtRetailAfterSpentMinus.text = saleToday.retailAfterSpentMinus
            txtSubmittedBy.text = saleToday.submittedBy
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}