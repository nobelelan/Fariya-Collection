package com.example.fariyafardinfarhancollection.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fariyafardinfarhancollection.databinding.ActivitySignupBinding
import com.example.fariyafardinfarhancollection.model.Employee
import com.example.fariyafardinfarhancollection.verifyDataFromUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener{
            sendRegistrationInfoToAdmin()
        }
    }

    private fun sendRegistrationInfoToAdmin() {

        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val password2 = binding.edtPassword2.text.toString()

        if (verifyDataFromUser(username, email, password, password2)){
            val employee = Employee(username, email, password)
            Firebase.firestore.collection("toRegisterEmployees").add(employee)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data sent successfully! Registration on going, sign in after 24 hours.", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Log.d("SignupActivity","Error adding document: $it")
                }

        }else{
            Toast.makeText(this, "Fill out all the fields properly!", Toast.LENGTH_SHORT).show()
        }
    }
}