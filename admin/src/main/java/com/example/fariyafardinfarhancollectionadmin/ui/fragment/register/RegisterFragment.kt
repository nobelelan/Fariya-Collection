package com.example.fariyafardinfarhancollectionadmin.ui.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fariyafardinfarhancollectionadmin.R
import com.example.fariyafardinfarhancollectionadmin.databinding.FragmentRegisterBinding
import com.example.fariyafardinfarhancollectionadmin.model.Employee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val registerAdapter by lazy { RegisterAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)

        auth = Firebase.auth

        val recyclerView = binding.rvRegisterRequestedEmployees
        recyclerView.adapter = registerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Firebase.firestore.collection("toRegisterEmployees").get()
            .addOnSuccessListener { employeeSnapshot ->
                val registerRequestEmployeeList = arrayListOf<Employee>()
                employeeSnapshot.forEach {
                    val employee = it.toObject(Employee::class.java)
                    registerRequestEmployeeList.add(employee)
                }
                registerAdapter.differ.submitList(registerRequestEmployeeList)
            }

        registerAdapter.setOnItemClickListener { employee ->
            auth.createUserWithEmailAndPassword(employee.email, employee.password)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "${employee.username} successfully registered!", Toast.LENGTH_SHORT).show()
                    auth.currentUser?.let {
                        val registeredEmployee = Employee(employee.username, employee.email, employee.contact)
                        val registeredDocumentReference = Firebase.firestore.collection("registeredEmployees").document(it.uid)
                        registeredDocumentReference.set(registeredEmployee)
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "Registration failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }

}