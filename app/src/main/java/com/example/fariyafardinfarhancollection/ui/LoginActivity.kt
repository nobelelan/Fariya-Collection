package com.example.fariyafardinfarhancollection.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fariyafardinfarhancollection.databinding.ActivityLoginBinding
import com.example.fariyafardinfarhancollection.verifyLoginDataFromUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (verifyLoginDataFromUser(email, password)){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        checkUserAccess(authResult.user?.uid)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Sign in failed!", Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this, "Fill out all the fields properly!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUserAccess(uid: String?) {
        uid?.let {
            val documentSnapshot = Firebase.firestore.collection("registeredEmployees").document(uid)
            documentSnapshot.get().addOnSuccessListener {
                if (it.getString("employee") == "yes"){
                    finish()
                    Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                }else{
                    Toast.makeText(this, "Sorry, you don't have access in employee module.", Toast.LENGTH_LONG).show()
                    auth.signOut()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}