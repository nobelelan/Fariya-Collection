package com.example.fariyafardinfarhancollectionadmin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollectionadmin.databinding.RvTestActivityBinding
import com.example.fariyafardinfarhancollectionadmin.model.Employee

class TestActivityAdapter: RecyclerView.Adapter<TestActivityAdapter.TestActivityViewHolder>() {

    inner class TestActivityViewHolder(val binding: RvTestActivityBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Employee>(){
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestActivityViewHolder {
        return TestActivityViewHolder(RvTestActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TestActivityViewHolder, position: Int) {
        val employee = differ.currentList[position]

        holder.binding.txtUsername.text = employee.username
        holder.binding.txtEmail.text = employee.email
        holder.binding.txtContact.text = employee.contact
        holder.binding.btnRegisterUser.setOnClickListener {
            onItemClickListener?.let { it(employee) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Employee) -> Unit)? = null

    fun setOnItemClickListener(listener: (Employee) -> Unit){
        onItemClickListener = listener
    }
}