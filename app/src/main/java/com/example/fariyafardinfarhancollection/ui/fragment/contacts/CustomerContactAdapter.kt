package com.example.fariyafardinfarhancollection.ui.fragment.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.databinding.RvCustomerContactBinding
import com.example.fariyafardinfarhancollection.model.CustomerContact

class CustomerContactAdapter: RecyclerView.Adapter<CustomerContactAdapter.CustomerContactViewHolder>() {

    inner class CustomerContactViewHolder(val binding: RvCustomerContactBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<CustomerContact>(){
        override fun areItemsTheSame(oldItem: CustomerContact, newItem: CustomerContact): Boolean {
            return oldItem.ccId == newItem.ccId
        }

        override fun areContentsTheSame(oldItem: CustomerContact, newItem: CustomerContact): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerContactViewHolder {
        return CustomerContactViewHolder(RvCustomerContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CustomerContactViewHolder, position: Int) {
        val customerContact = differ.currentList[position]

        holder.binding.apply {
            txtCustomerName.text = customerContact.name
            txtCustomerPhoneNumber.text = customerContact.number
            txtCustomerAddress.text = customerContact.address
            txtCustomerDue.text = customerContact.due.toString()
        }
        holder.binding.imgEdit.setOnClickListener {
            onEditItemClickListener?.let { it(customerContact) }
        }
        holder.binding.imgMakeCall.setOnClickListener {
            customerContact?.let {
                makeCallClickListener?.onMakeCallClick(it.number.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var makeCallClickListener: ClickListener? = null
    interface ClickListener{
        fun onMakeCallClick(customerNumber: String)
    }
    fun setMakeCallClickListener(listener: ClickListener){
        makeCallClickListener = listener
    }

    private var onEditItemClickListener: ((CustomerContact)->Unit) ?= null
    fun setOnEditClickListener(listener: (CustomerContact)->Unit){
        onEditItemClickListener = listener
    }
}