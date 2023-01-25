package com.example.fariyafardinfarhancollectionadmin.ui.fragment.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollectionadmin.databinding.RvSalesRecordItemBinding
import com.example.fariyafardinfarhancollectionadmin.model.SaleRecords

class RecordsAdapter: RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>() {

    inner class RecordsViewHolder(val binding: RvSalesRecordItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<SaleRecords>(){
        override fun areItemsTheSame(oldItem: SaleRecords, newItem: SaleRecords): Boolean {
            return oldItem.saleRecordId == newItem.saleRecordId
        }

        override fun areContentsTheSame(oldItem: SaleRecords, newItem: SaleRecords): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        return RecordsViewHolder(RvSalesRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        val saleRecords = differ.currentList[position]

        holder.binding.apply {
            txtCurrentDate.text = saleRecords.date
            txtProductCountSale.text = saleRecords.retailSale
            txtRetailTotal.text = saleRecords.retailTotal
            txtWholesaleSale.text = saleRecords.wholesale
            txtWholesaleTotal.text = saleRecords.wholesaleTotal
            txtOtherPaymentAmount.text = saleRecords.otherPayment
            txtOtherPaymentTotal.text = saleRecords.otherPaymentTotal
            txtSpentTodayAmount.text = saleRecords.spentToday
            txtSpentTodayTotal.text = saleRecords.spentTodayTotal
            txtComment.text = saleRecords.comment
            txtRetailAfterSpentMinus.text = saleRecords.retailAfterSpentMinus
            txtSubmittedBy.text = saleRecords.submittedBy
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}