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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class StorehouseFragment : Fragment() {

    private var _binding: FragmentStorehouseBinding? = null
    private val binding get() = _binding!!

    private lateinit var shopViewModel: ShopViewModel

    private val storeProductAdapter by lazy { StoreProductAdapter() }

    private val storeProductsCollectionRef = Firebase.firestore.collection("storeProducts")
    private val storeProductsCounterCollectionRef = Firebase.firestore.collection("allCounters")

    private var databaseContactsCounter: Int? = null

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

//        shopViewModel.getAllStoreProduct.observe(viewLifecycleOwner, Observer {
//            storeProductAdapter.differ.submitList(it)
//        })

        storeProductsCollectionRef
            .orderBy("storeProductId", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let {
                if (it.documents.isNotEmpty()){
                    val productList = it.toObjects<StoreProduct>()
                    storeProductAdapter.differ.submitList(productList)
                    binding.txtNoProducts.visibility = View.INVISIBLE
                }else{
                    binding.txtNoProducts.visibility = View.VISIBLE
                }
            }
        }

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
                val productName = spBinding.edtProductName.text.toString()
                val quantityLeft = spBinding.edtQuantityLeft.text.toString()
                val retailPrice = spBinding.edtRetailPrice.text.toString()
                val wholesalePrice = spBinding.edtWholesalePrice.text.toString()
                if (verifyProductInformation(productName, quantityLeft, retailPrice, wholesalePrice)){
                    storeProductsCollectionRef
                        .whereEqualTo("storeProductId", it.storeProductId)
                        .whereEqualTo("productName", it.productName)
                        .whereEqualTo("quantityLeft", it.quantityLeft)
                        .whereEqualTo("retailPrice", it.retailPrice)
                        .whereEqualTo("wholesalePrice", it.wholesalePrice)
                        .get()
                        .addOnSuccessListener { querySnapshot->
                            if (querySnapshot.documents.isNotEmpty()){
                                querySnapshot.forEach { documentSnapshot->
                                    Firebase.firestore.runTransaction { transaction->
                                        val productsRef = storeProductsCollectionRef.document(documentSnapshot.id)
                                        transaction.get(productsRef)
                                        transaction.update(productsRef, "productName", productName)
                                        transaction.update(productsRef, "quantityLeft", quantityLeft)
                                        transaction.update(productsRef, "retailPrice", retailPrice)
                                        transaction.update(productsRef, "wholesalePrice", wholesalePrice)
                                        null
                                    }.addOnSuccessListener { nothing->
//                                        shopViewModel.updateStoreProduct(StoreProduct(it.storeProductId, productName, quantityLeft, retailPrice, wholesalePrice))
                                        Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener{e->
                                        Toast.makeText(requireContext(), "Something went wrong!:${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                        }
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
                // Deletion from room db
//                val tobeDeletedItem = storeProductAdapter.differ.currentList[viewHolder.adapterPosition]
//                shopViewModel.deleteStoreProduct(tobeDeletedItem)
//                storeProductAdapter.notifyItemRemoved(viewHolder.adapterPosition)
//                Snackbar.make(viewHolder.itemView, "Product Deleted!", Snackbar.LENGTH_LONG)
//                    .setAction("Undo"){ shopViewModel.insertStoreProduct(tobeDeletedItem) }
//                    .show()

                // Deletion from firestore
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Product!")
                builder.setMessage("Product information can never be restored.")
                builder.setNegativeButton("Cancel"){_,_->}
                builder.setPositiveButton("Delete"){_,_->
                    val tobeDeletedItem = storeProductAdapter.differ.currentList[viewHolder.adapterPosition]
                    storeProductsCollectionRef
                        .whereEqualTo("storeProductId", tobeDeletedItem.storeProductId)
                        .whereEqualTo("productName", tobeDeletedItem.productName)
                        .whereEqualTo("quantityLeft", tobeDeletedItem.quantityLeft)
                        .whereEqualTo("retailPrice", tobeDeletedItem.retailPrice)
                        .whereEqualTo("wholesalePrice", tobeDeletedItem.wholesalePrice)
                        .get()
                        .addOnSuccessListener { querySnapshot->
                            if (querySnapshot.documents.isNotEmpty()){
                                querySnapshot?.forEach { documentSnapshot->
                                    storeProductsCollectionRef.document(documentSnapshot.id).delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(requireContext(), "Successfully Deleted!", Toast.LENGTH_SHORT).show()
                                        }.addOnFailureListener {
                                            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        }
                }.create().show()
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
                val productName = spBinding.edtProductName.text.toString()
                val quantityLeft = spBinding.edtQuantityLeft.text.toString()
                val retailPrice = spBinding.edtRetailPrice.text.toString()
                val wholesalePrice = spBinding.edtWholesalePrice.text.toString()
                if (verifyProductInformation(productName, quantityLeft, retailPrice, wholesalePrice)){
                    Firebase.firestore.runTransaction { transaction->
                        val counterRef = storeProductsCounterCollectionRef.document("storeProductsCounter")
                        val counter = transaction.get(counterRef)
                        val newCounter = counter["storeProductId"] as Long + 1
                        databaseContactsCounter = newCounter.toInt()
                        transaction.update(counterRef,"storeProductId", newCounter)

                        storeProductsCollectionRef.document().set(StoreProduct(
                            newCounter.toInt(),
                            productName,
                            quantityLeft,
                            retailPrice,
                            wholesalePrice
                        ))
                        null
                    }.addOnSuccessListener {
//                        shopViewModel.insertStoreProduct(StoreProduct(0, productName, quantityLeft, retailPrice, wholesalePrice))
                        Toast.makeText(requireContext(), "New Product Inserted!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "Please fill out required fields!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.create().show()
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

}