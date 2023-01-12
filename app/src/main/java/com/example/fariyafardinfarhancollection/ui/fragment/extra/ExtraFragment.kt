package com.example.fariyafardinfarhancollection.ui.fragment.extra

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.SwipeToDelete
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.databinding.DialogEditPublicPostBinding
import com.example.fariyafardinfarhancollection.databinding.DialogUpsertCustomerContactBinding
import com.example.fariyafardinfarhancollection.databinding.FragmentExtraBinding
import com.example.fariyafardinfarhancollection.model.Employee
import com.example.fariyafardinfarhancollection.model.PublicPost
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.ui.LoginActivity
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class ExtraFragment : Fragment() {

    private var _binding: FragmentExtraBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val publicPostAdapter by lazy { PublicPostAdapter() }

    private lateinit var shopViewModel: ShopViewModel

    private var currentEmployee: Employee? = null

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

        val shopDao = ShopDatabase.getDatabase(requireContext()).shopDao()
        val shopRepository = ShopRepository(shopDao)
        val shopViewModelProviderFactory = ShopViewModelProviderFactory(requireActivity().application, shopRepository)
        shopViewModel = ViewModelProvider(this, shopViewModelProviderFactory)[ShopViewModel::class.java]

        setEmployeeProfile()

        setUpPublicPostRecyclerView()

        shopViewModel.getAllPublicPost.observe(viewLifecycleOwner, Observer {
            publicPostAdapter.differ.submitList(it)
        })

        binding.txtSignOut.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Signing off...")
            builder.setNegativeButton("Cancel"){_,_->}
            builder.setPositiveButton("Continue"){_,_->
                auth.signOut()
                activity?.finish()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }.create().show()
        }
        binding.btnCurrencyConverter.setOnClickListener {
            findNavController().navigate(R.id.action_extraFragment_to_currencyConverterFragment)
        }

        publicPostAdapter.setOnItemClickListener(object : PublicPostAdapter.OnItemClickListener{
            override fun onEditClick(publicPost: PublicPost) {
                val inflater = LayoutInflater.from(requireContext())
                val ppBinding = DialogEditPublicPostBinding.inflate(inflater)

                ppBinding.edtPost.setText(publicPost.post)

                val builder = AlertDialog.Builder(requireContext())
                builder.setView(ppBinding.root)
                builder.setNegativeButton("Cancel"){_,_->}
                builder.setPositiveButton("Done"){_,_->
                    val updatedPost = ppBinding.edtPost.text.toString()
                    shopViewModel.updatePublicPost(PublicPost(publicPost.publicPostId,publicPost.employeeName, publicPost.dateAndTime, updatedPost))
                    Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
                builder.create().show()
            }

            override fun onDeleteClick(publicPost: PublicPost) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete post?")
                builder.setNegativeButton("No"){_,_->}
                builder.setPositiveButton("Yes"){_,_->
                    shopViewModel.deletePublicPost(publicPost)
                    Toast.makeText(requireContext(), "Post deleted successfully!", Toast.LENGTH_SHORT).show()
                }
                builder.create().show()
            }

            override fun onViewSet(editView: ImageView, deleteView: ImageView) {
                shopViewModel.getAllPublicPost.observe(viewLifecycleOwner, Observer {
//                    it.forEach { publicPost ->
//                        if (currentEmployee?.username == publicPost.employeeName){
//                            editView.visibility = View.VISIBLE
//                            deleteView.visibility = View.VISIBLE
//                        }else{
//                            editView.visibility = View.INVISIBLE
//                            deleteView.visibility = View.INVISIBLE
//                        }
//                    }
                    editView.visibility = View.VISIBLE
                    deleteView.visibility = View.VISIBLE
                })
            }
        })
    }

    private fun setEmployeeProfile() {
        val documentSnapshot = Firebase.firestore.collection("registeredEmployees").document(auth.currentUser!!.uid)
        documentSnapshot.get().addOnSuccessListener {
            currentEmployee = it.toObject<Employee>()
            currentEmployee?.let { employee->
                binding.textEmployeeName.text = employee.username
                binding.textEmployeeEmail.text = employee.email
                binding.textEmployeeContact.text = employee.contact
            }
        }
    }

    private fun setUpPublicPostRecyclerView() {
        val rvPublicPost = binding.rvPublicPost
        rvPublicPost.adapter = publicPostAdapter
        rvPublicPost.layoutManager = LinearLayoutManager(requireContext())

        binding.btnCreatePost.setOnClickListener {
            val calender = Calendar.getInstance()
            val time = calender.time

            val employeeName = currentEmployee?.username
            val dateAndTime = "$time"
            val post = binding.edtCreatePost.text.toString()

            shopViewModel.insertPublicPost(PublicPost(0, employeeName, dateAndTime, post))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}