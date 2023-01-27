package com.example.fariyafardinfarhancollectionadmin.ui.fragment.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fariyafardinfarhancollectionadmin.R
import com.example.fariyafardinfarhancollectionadmin.databinding.FragmentAttendanceBinding
import com.example.fariyafardinfarhancollectionadmin.model.Employee
import com.example.fariyafardinfarhancollectionadmin.model.EmployeeAttendance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val attendanceAdapter by lazy { AttendanceAdapter() }

    // TODO: this fragment need a lot of work, regarding data fetching, employee name management 
    private val employeeUids = arrayListOf<String>()
    private val employeeList = arrayListOf<Employee>()
    private val employeeAttendance = arrayListOf<EmployeeAttendance>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAttendanceBinding.bind(view)

        auth = Firebase.auth

        setUpAttendanceRecyclerView()

        fetchEmployeeUids()

        fetchEmployeeDetailsOnUid()
    }

    private fun fetchEmployeeDetailsOnUid() {
        employeeUids.forEach{
            Firebase.firestore.collection("registeredEmployees").document(it)
                .get()
                .addOnSuccessListener { documentSnapshot->
                    val currentEmployee = documentSnapshot.toObject<Employee>()
                    employeeList.add(currentEmployee!!)
                }
                .addOnFailureListener{ e ->
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            Firebase.firestore.collection("employeeAttendance").document(it).collection("day")
                .get()
                .addOnSuccessListener { querySnapshot->
                    if (querySnapshot.documents.isNotEmpty()){
                        querySnapshot.forEach { documentSnapshot->
                            employeeAttendance.add(documentSnapshot.toObject<EmployeeAttendance>())
                        }
                    }
                }
        }
    }

    private fun fetchEmployeeUids() {
        Firebase.firestore.collection("employeeAttendance").get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()){
                    querySnapshot.forEach {
                        employeeUids.add(it.toString())
                    }
                }
            }
    }

    private fun setUpAttendanceRecyclerView() {
        val rvAttendance = binding.rvAttendance
        rvAttendance.adapter = attendanceAdapter
        rvAttendance.layoutManager = LinearLayoutManager(requireContext())
    }

}