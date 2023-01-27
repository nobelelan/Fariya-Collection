package com.example.fariyafardinfarhancollectionadmin.ui.fragment.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fariyafardinfarhancollectionadmin.R
import com.example.fariyafardinfarhancollectionadmin.databinding.FragmentPostBinding
import com.example.fariyafardinfarhancollectionadmin.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val postAdapter by lazy { PostAdapter() }

    private val postCollectionRef = Firebase.firestore.collection("publicPosts")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostBinding.bind(view)

        auth = Firebase.auth

        setUpPostRecyclerView()

        fetchPostFromFirestore()
    }

    private fun fetchPostFromFirestore() {
        postCollectionRef.addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()){
                    val postList = querySnapshot.toObjects<Post>()
                    postAdapter.differ.submitList(postList)
                }
            }
        }
    }

    private fun setUpPostRecyclerView() {
        val rvPost = binding.rvPost
        rvPost.adapter = postAdapter
        rvPost.layoutManager = LinearLayoutManager(requireContext())
    }

}