package com.example.fariyafardinfarhancollection.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fariyafardinfarhancollection.databinding.ActivityTestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            finish()
        }
    }
}