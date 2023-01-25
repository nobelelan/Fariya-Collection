package com.example.fariyafardinfarhancollectionadmin.ui.fragment.records

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fariyafardinfarhancollectionadmin.R
import com.example.fariyafardinfarhancollectionadmin.databinding.FragmentRecordsBinding
import com.example.fariyafardinfarhancollectionadmin.model.SaleRecords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class RecordsFragment : Fragment() {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val recordsAdapter by lazy { RecordsAdapter() }

    private val recordsCollectionRef = Firebase.firestore.collection("saleTodays")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecordsBinding.bind(view)

        auth = Firebase.auth

        setUpRecordsRecyclerView()

        fetchRecordsFromFirestore()

        setHasOptionsMenu(true)
    }

    private fun fetchRecordsFromFirestore() {
        recordsCollectionRef.addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()){
                    val recordsList = querySnapshot.toObjects<SaleRecords>()
                    recordsAdapter.differ.submitList(recordsList)
                }
            }
        }
    }

    private fun setUpRecordsRecyclerView() {
        val recordsRecyclerView = binding.rvRecords
        recordsRecyclerView.adapter = recordsAdapter
        recordsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sales_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_register_users ->{
                findNavController().navigate(R.id.action_recordsFragment_to_registerFragment)
            }
            R.id.menu_sign_off ->{
                AlertDialog.Builder(requireContext())
                    .setMessage("Are you sure you want to sign off ?")
                    .setNegativeButton("No"){_,_->}
                    .setPositiveButton("Yes"){_,_->
                        auth.signOut()
                        activity?.finish()
                    }.create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}