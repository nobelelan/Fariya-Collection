package com.example.fariyafardinfarhancollection.ui.fragment.extra

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.databinding.FragmentExtraBinding
import com.example.fariyafardinfarhancollection.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ExtraFragment : Fragment() {

    private var _binding: FragmentExtraBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extra, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExtraBinding.bind(view)

        auth = Firebase.auth

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            activity?.finish()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}