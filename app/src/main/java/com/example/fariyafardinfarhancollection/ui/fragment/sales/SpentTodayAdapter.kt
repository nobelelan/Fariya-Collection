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
import com.example.fariyafardinfarhancollection.databinding.RvOtherPaymentReceivedBinding
import com.example.fariyafardinfarhancollection.databinding.RvSalesProductCountBinding
import com.example.fariyafardinfarhancollection.databinding.RvSpentTodayBinding
import com.example.fariyafardinfarhancollection.model.OtherPaymentReceived
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.model.SpentToday
import com.example.fariyafardinfarhancollection.verifyProductCountDataFromUser

class SpentTodayAdapter :
    RecyclerView.Adapter<SpentTodayAdapter.SpentTodayViewHolder>() {

    inner class SpentTodayViewHolder(val binding: RvSpentTodayBinding) :
        RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<SpentToday>() {
        override fun areItemsTheSame(oldItem: SpentToday, newItem: SpentToday): Boolean {
            return oldItem.spentTodayId == newItem.spentTodayId
        }

        override fun areContentsTheSame(oldItem: SpentToday, newItem: SpentToday): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpentTodayViewHolder {
        return SpentTodayViewHolder(
            RvSpentTodayBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SpentTodayViewHolder, position: Int) {
        val spentToday = differ.currentList[position]
        holder.binding.apply {
            edtReason.setText(spentToday.reason)
            edtAmount.setText(spentToday.amount)
        }
        holder.binding.txtUpdate.setOnClickListener {
            val reason = holder.binding.edtReason.text.toString()
            val amount = holder.binding.edtAmount.text.toString()
            if (verifyProductCountDataFromUser(reason, amount, amount)){
                updateItemListener?.updateProductCount(spentToday.spentTodayId, reason, amount)
                Toast.makeText(holder.itemView.context, "Spent Money Updated!", Toast.LENGTH_SHORT).show()
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
            spentTodayId: Int,
            reason: String,
            amount: String
        )
    }

    fun setUpdateItemListener(listener: UpdateItemListener) {
        updateItemListener = listener
    }
}