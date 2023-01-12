package com.example.fariyafardinfarhancollection.ui.fragment.extra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.databinding.RvPublicPostBinding
import com.example.fariyafardinfarhancollection.model.PublicPost

class PublicPostAdapter: RecyclerView.Adapter<PublicPostAdapter.PublicPostViewHolder>() {

    inner class PublicPostViewHolder(val binding: RvPublicPostBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<PublicPost>(){
        override fun areItemsTheSame(oldItem: PublicPost, newItem: PublicPost): Boolean {
            return oldItem.publicPostId == newItem.publicPostId
        }

        override fun areContentsTheSame(oldItem: PublicPost, newItem: PublicPost): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicPostViewHolder {
        return PublicPostViewHolder(RvPublicPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PublicPostViewHolder, position: Int) {
        val publicPost = differ.currentList[position]

        holder.binding.apply {
            txtEmployeeName.text = publicPost.employeeName
            txtDateAndTime.text = publicPost.dateAndTime
            txtPost.text = publicPost.post
        }

//        holder.binding.imgEditPost.setOnClickListener {
//            itemClickListener!!.onEditClick(publicPost)
//        }
//        holder.binding.imgDeletePost.setOnClickListener {
//            itemClickListener!!.onDeleteClick(publicPost)
//        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // TODO: needs to set user specific visibilit for updation or deletion
//    private var itemClickListener: OnItemClickListener? = null
//
//    fun setOnItemClickListener(listener: OnItemClickListener){
//        itemClickListener = listener
//    }
//
//    interface OnItemClickListener{
//        fun onEditClick(publicPost: PublicPost)
//        fun onDeleteClick(publicPost: PublicPost)
//    }
}