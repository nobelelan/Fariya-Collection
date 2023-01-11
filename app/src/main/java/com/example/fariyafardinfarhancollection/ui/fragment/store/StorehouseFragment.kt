package com.example.fariyafardinfarhancollection.ui.fragment.store

import android.app.AlertDialog
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.SwipeToDelete
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.databinding.DialogUpsertCustomerContactBinding
import com.example.fariyafardinfarhancollection.databinding.DialogUpsertProductStoreItemBinding
import com.example.fariyafardinfarhancollection.databinding.FragmentStorehouseBinding
import com.example.fariyafardinfarhancollection.databinding.RvProductStoreBinding
import com.example.fariyafardinfarhancollection.model.CustomerContact
import com.example.fariyafardinfarhancollection.model.StoreProduct
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.ui.fragment.contacts.CustomerContactAdapter
import com.example.fariyafardinfarhancollection.verifyCustomerInformation
import com.example.fariyafardinfarhancollection.verifyProductInformation
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar


class StorehouseFragment : Fragment() {

    private var _binding: FragmentStorehouseBinding? = null
    private val binding get() = _binding!!

    private lateinit var shopViewModel: ShopViewModel

    private val storeProductAdapter by lazy { StoreProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storehouse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStorehouseBinding.bind(view)

        val shopDao = ShopDatabase.getDatabase(requireContext()).shopDao()
        val shopRepository = ShopRepository(shopDao)
        val shopViewModelProviderFactory = ShopViewModelProviderFactory(requireActivity().application, shopRepository)
        shopViewModel = ViewModelProvider(this, shopViewModelProviderFactory)[ShopViewModel::class.java]

        setUpStoreProductRecyclerView()

        shopViewModel.getAllStoreProduct.observe(viewLifecycleOwner, Observer {
            storeProductAdapter.differ.submitList(it)
        })

        storeProductAdapter.setOnEditClickListener {
            val inflater = LayoutInflater.from(requireContext())
            val spBinding = DialogUpsertProductStoreItemBinding.inflate(inflater)

            spBinding.edtProductName.setText(it.productName)
            spBinding.edtQuantityLeft.setText(it.quantityLeft)
            spBinding.edtRetailPrice.setText(it.retailPrice)
            spBinding.edtWholesalePrice.setText(it.wholesalePrice)

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(spBinding.root)
            builder.setTitle("Product information")
            builder.setNegativeButton("Cancel"){_,_->}
            builder.setPositiveButton("Done"){_,_->
                val customerName = spBinding.edtProductName.text.toString()
                val quantityLeft = spBinding.edtQuantityLeft.text.toString()
                val retailPrice = spBinding.edtRetailPrice.text.toString()
                val wholesalePrice = spBinding.edtWholesalePrice.text.toString()
                if (verifyProductInformation(customerName, quantityLeft, retailPrice, wholesalePrice)){
                    shopViewModel.updateStoreProduct(StoreProduct(it.storeProductId, customerName, quantityLeft, retailPrice, wholesalePrice))
                    Toast.makeText(requireContext(), "New Product Inserted!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Please fill out required fields!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.create().show()
        }
    }

    private fun setUpStoreProductRecyclerView() {
        val rvStoreProduct = binding.rvStoreProduct
        rvStoreProduct.adapter = storeProductAdapter
        rvStoreProduct.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tobeDeletedItem = storeProductAdapter.differ.currentList[viewHolder.adapterPosition]
                shopViewModel.deleteStoreProduct(tobeDeletedItem)
                storeProductAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(viewHolder.itemView, "Product Deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){ shopViewModel.insertStoreProduct(tobeDeletedItem) }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvStoreProduct)

        binding.fabAddProduct.setOnClickListener {
            val inflater = LayoutInflater.from(requireContext())
            val spBinding = DialogUpsertProductStoreItemBinding.inflate(inflater)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(spBinding.root)
            builder.setTitle("Product Information")
            builder.setNegativeButton("Cancel"){_,_->}
            builder.setPositiveButton("Done"){_,_->
                val customerName = spBinding.edtProductName.text.toString()
                val quantityLeft = spBinding.edtQuantityLeft.text.toString()
                val retailPrice = spBinding.edtRetailPrice.text.toString()
                val wholesalePrice = spBinding.edtWholesalePrice.text.toString()
                if (verifyProductInformation(customerName, quantityLeft, retailPrice, wholesalePrice)){
                    shopViewModel.insertStoreProduct(StoreProduct(0, customerName, quantityLeft, retailPrice, wholesalePrice))
                    Toast.makeText(requireContext(), "New Product Inserted!", Toast.LENGTH_SHORT).show()
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