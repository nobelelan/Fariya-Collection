package com.example.fariyafardinfarhancollectionadmin.ui.fragment.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollectionadmin.databinding.RvEmployeeAttendanceBinding
import com.example.fariyafardinfarhancollectionadmin.model.EmployeeAttendance

class AttendanceAdapter: RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(val binding: RvEmployeeAttendanceBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<EmployeeAttendance>(){
        override fun areItemsTheSame(
            oldItem: EmployeeAttendance,
            newItem: EmployeeAttendance
        ): Boolean {
            return oldItem.attendanceId == newItem.attendanceId
        }

        override fun areContentsTheSame(
            oldItem: EmployeeAttendance,
            newItem: EmployeeAttendance
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(RvEmployeeAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val employeeAttendance = differ.currentList[position]

        holder.binding.apply {
            txtDate.text = employeeAttendance.date
            txtTime.text = employeeAttendance.time
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}