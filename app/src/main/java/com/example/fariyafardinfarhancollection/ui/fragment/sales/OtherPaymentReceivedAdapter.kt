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
import com.example.fariyafardinfarhancollection.model.OtherPaymentReceived
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.verifyProductCountDataFromUser

class OtherPaymentReceivedAdapter :
    RecyclerView.Adapter<OtherPaymentReceivedAdapter.OtherPaymentReceivedViewHolder>() {

    inner class OtherPaymentReceivedViewHolder(val binding: RvOtherPaymentReceivedBinding) :
        RecyclerView.ViewHolder(binding.root)

    val differCallBack = object : DiffUtil.ItemCallback<OtherPaymentReceived>() {
        override fun areItemsTheSame(oldItem: OtherPaymentReceived, newItem: OtherPaymentReceived): Boolean {
            return oldItem.otherPaymentId == newItem.otherPaymentId
        }

        override fun areContentsTheSame(oldItem: OtherPaymentReceived, newItem: OtherPaymentReceived): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherPaymentReceivedViewHolder {
        return OtherPaymentReceivedViewHolder(
            RvOtherPaymentReceivedBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OtherPaymentReceivedViewHolder, position: Int) {
        val otherPaymentReceived = differ.currentList[position]
        holder.binding.apply {
            edtSenderName.setText(otherPaymentReceived.senderName)
            edtPaymentMethod.setText(otherPaymentReceived.paymentMethod)
            edtAmount.setText(otherPaymentReceived.amount)
        }
        holder.binding.txtUpdate.setOnClickListener {
            val senderName = holder.binding.edtSenderName.text.toString()
            val paymentMethod = holder.binding.edtPaymentMethod.text.toString()
            val amount = holder.binding.edtAmount.text.toString()
            if (verifyProductCountDataFromUser(senderName, paymentMethod, amount)){
                updateItemListener?.updateProductCount(otherPaymentReceived.otherPaymentId, senderName, paymentMethod, amount)
                Toast.makeText(holder.itemView.context, "Payment Updated!", Toast.LENGTH_SHORT).show()
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
            otherPaymentId: Int,
            senderName: String,
            paymentMethod: String,
            amount: String
        )
    }

    fun setUpdateItemListener(listener: UpdateItemListener) {
        updateItemListener = listener
    }
}