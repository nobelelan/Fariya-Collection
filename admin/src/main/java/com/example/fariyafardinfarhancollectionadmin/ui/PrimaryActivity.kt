package com.example.fariyafardinfarhancollectionadmin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fariyafardinfarhancollectionadmin.R
import com.example.fariyafardinfarhancollectionadmin.databinding.ActivityPrimaryBinding
import com.example.fariyafardinfarhancollectionadmin.model.Employee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PrimaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrimaryBinding

    private lateinit var auth: FirebaseAuth

    private val testActivityAdapter by lazy { TestActivityAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrimaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            finish()
        }

        val recyclerView = binding.rvRegisterRequestedEmployees
        recyclerView.adapter = testActivityAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        Firebase.firestore.collection("toRegisterEmployees").get()
            .addOnSuccessListener { employeeSnapshot ->
                val registerRequestEmployeeList = arrayListOf<Employee>()
                employeeSnapshot.forEach {
                    val employee = it.toObject(Employee::class.java)
                    registerRequestEmployeeList.add(employee)
                }
                testActivityAdapter.differ.submitList(registerRequestEmployeeList)
            }

        testActivityAdapter.setOnItemClickListener { employee ->
            auth.createUserWithEmailAndPassword(employee.email, employee.password)
                .addOnSuccessListener {
                    Toast.makeText(this, "${employee.username} successfully registered!", Toast.LENGTH_SHORT).show()
                    auth.currentUser?.let {
                        val registeredEmployee = Employee(employee.username, employee.email, employee.contact)
                        val registeredDocumentReference = Firebase.firestore.collection("registeredEmployees").document(it.uid)
                        registeredDocumentReference.set(registeredEmployee)
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}