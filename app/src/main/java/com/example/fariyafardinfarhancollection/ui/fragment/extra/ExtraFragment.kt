package com.example.fariyafardinfarhancollection.ui.fragment.extra

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.SwipeToDelete
import com.example.fariyafardinfarhancollection.database.ShopDatabase
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
            auth.signOut()
            activity?.finish()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.btnCurrencyConverter.setOnClickListener {
            findNavController().navigate(R.id.action_extraFragment_to_currencyConverterFragment)
        }
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

        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tobeDeletedItem = publicPostAdapter.differ.currentList[viewHolder.adapterPosition]
                shopViewModel.deletePublicPost(tobeDeletedItem)
                publicPostAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(viewHolder.itemView, "Post Deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){ shopViewModel.insertPublicPost(tobeDeletedItem) }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvPublicPost)

        binding.btnCreatePost.setOnClickListener {
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val time = calender.time

            val employeeName = currentEmployee?.username
            val dateAndTime = "$time : $day/${month + 1}/$year"
            val post = binding.edtCreatePost.text.toString()

            shopViewModel.insertPublicPost(PublicPost(0, employeeName, dateAndTime, post))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}