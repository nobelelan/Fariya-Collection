package com.example.fariyafardinfarhancollection.ui.fragment.contacts

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.SwipeToDelete
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.databinding.DialogUpsertCustomerContactBinding
import com.example.fariyafardinfarhancollection.databinding.FragmentCustomerContactsBinding
import com.example.fariyafardinfarhancollection.model.CustomerContact
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.verifyCustomerInformation
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class CustomerContactsFragment : Fragment() {

    private var _binding: FragmentCustomerContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var shopViewModel: ShopViewModel

    private val customerContactAdapter by lazy { CustomerContactAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomerContactsBinding.bind(view)

        val shopDao = ShopDatabase.getDatabase(requireContext()).shopDao()
        val shopRepository = ShopRepository(shopDao)
        val shopViewModelProviderFactory = ShopViewModelProviderFactory(requireActivity().application, shopRepository)
        shopViewModel = ViewModelProvider(this, shopViewModelProviderFactory)[ShopViewModel::class.java]

        setUpCustomerContactsRecyclerView()

        shopViewModel.getAllCustomerContacts.observe(viewLifecycleOwner, Observer {
            customerContactAdapter.differ.submitList(it)
        })

        customerContactAdapter.setOnEditClickListener {
            val inflater = LayoutInflater.from(requireContext())
            val ccBinding = DialogUpsertCustomerContactBinding.inflate(inflater)

            ccBinding.edtCustomerName.setText(it.name)
            ccBinding.edtCustomerContact.setText(it.number.toString())
            ccBinding.edtCustomerAddress.setText(it.address)
            ccBinding.edtCustomerDue.setText(it.due.toString())

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(ccBinding.root)
            builder.setTitle("Customer information")
            builder.setNegativeButton("Cancel"){_,_->}
            builder.setPositiveButton("Done"){_,_->
                val customerName = ccBinding.edtCustomerName.text.toString()
                val customerContact = ccBinding.edtCustomerContact.text.toString()
                val customerAddress = ccBinding.edtCustomerAddress.text.toString()
                val customerDue = ccBinding.edtCustomerDue.text.toString().toIntOrNull()
                if (verifyCustomerInformation(customerName, customerContact, customerAddress, customerDue.toString())){
                    shopViewModel.updateCustomerContact(CustomerContact(it.ccId, customerName, customerContact, customerAddress, customerDue))
                    Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Please fill out required fields!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.create().show()
        }
        customerContactAdapter.setMakeCallClickListener(object :CustomerContactAdapter.ClickListener{
            override fun onMakeCallClick(customerNumber: String) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customerNumber, null))
                activity?.startActivity(intent)
            }
        })
    }

    private fun setUpCustomerContactsRecyclerView() {
        val rvCustomerContact = binding.rvCustomerContact
        rvCustomerContact.adapter = customerContactAdapter
        rvCustomerContact.layoutManager = LinearLayoutManager(requireContext())

        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tobeDeletedItem = customerContactAdapter.differ.currentList[viewHolder.adapterPosition]
                shopViewModel.deleteCustomerContact(tobeDeletedItem)
                customerContactAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(viewHolder.itemView, "Contact Deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){ shopViewModel.insertCustomerContact(tobeDeletedItem) }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvCustomerContact)

        binding.fabAddContact.setOnClickListener {
            val inflater = LayoutInflater.from(requireContext())
            val ccBinding = DialogUpsertCustomerContactBinding.inflate(inflater)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(ccBinding.root)
            builder.setTitle("Customer information")
            builder.setNegativeButton("Cancel"){_,_->}
            builder.setPositiveButton("Done"){_,_->
                val customerName = ccBinding.edtCustomerName.text.toString()
                val customerContact = ccBinding.edtCustomerContact.text.toString()
                val customerAddress = ccBinding.edtCustomerAddress.text.toString()
                val customerDue = ccBinding.edtCustomerDue.text.toString().toIntOrNull()
                if (verifyCustomerInformation(customerName, customerContact, customerAddress, customerDue.toString())){
                    shopViewModel.insertCustomerContact(CustomerContact(0, customerName, customerContact, customerAddress, customerDue))
                    Toast.makeText(requireContext(), "New Contact Inserted!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Please fill out required fields!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}