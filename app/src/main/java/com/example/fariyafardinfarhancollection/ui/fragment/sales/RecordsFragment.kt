package com.example.fariyafardinfarhancollection.ui.fragment.sales

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.SwipeToDelete
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.databinding.FragmentRecordsBinding
import com.example.fariyafardinfarhancollection.model.SaleToday
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class RecordsFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private val salesAdapter by lazy { SalesAdapter() }

    private lateinit var shopViewModel: ShopViewModel

    private val recordsCollectionRef = Firebase.firestore.collection("saleTodays")

    private var searchView: SearchView? = null

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

        val shopDao = ShopDatabase.getDatabase(requireContext()).shopDao()
        val shopRepository = ShopRepository(shopDao)
        val shopViewModelProviderFactory = ShopViewModelProviderFactory(requireActivity().application, shopRepository)
        shopViewModel = ViewModelProvider(this, shopViewModelProviderFactory)[ShopViewModel::class.java]

        setUpRecyclerView()

        shopViewModel.getAllSaleToday.observe(viewLifecycleOwner, Observer {
            salesAdapter.differ.submitList(it)
        })

        recordsCollectionRef
            .orderBy("saleId", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                value?.let { querySnapshot ->
                    if (querySnapshot.documents.isNotEmpty()){
                        val saleTodayList = querySnapshot.toObjects<SaleToday>()
                        saleTodayList.forEach { saleToday ->
                            shopViewModel.insertSaleToday(saleToday)
                        }
//                        salesAdapter.differ.submitList(saleTodayList)
                    }
                }
            }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_records_fragment, menu)
        val search = menu.findItem(R.id.menu_search)
        searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun searchThroughDatabase(query: String){
        val searchQuery = "%$query%"
        shopViewModel.searchSaleToday(searchQuery).observe(this, Observer { saleTodayList->
            saleTodayList?.let {
                salesAdapter.differ.submitList(it)
            }
        })
        searchView?.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (!hasFocus){
                activity?.recreate()
            }
        }
    }

    private fun setUpRecyclerView() {

        val rvSales = binding.rvSales
        rvSales.adapter = salesAdapter
        rvSales.layoutManager = LinearLayoutManager(requireContext())

//        val swipeToDeleteCallback = object : SwipeToDelete(){
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val tobeDeletedItem = salesAdapter.differ.currentList[viewHolder.adapterPosition]
//                shopViewModel.deleteSaleToday(tobeDeletedItem)
//                salesAdapter.notifyItemRemoved(viewHolder.adapterPosition)
//                Snackbar.make(viewHolder.itemView, "Item Deleted!", Snackbar.LENGTH_LONG)
//                    .setAction("Undo"){ shopViewModel.insertSaleToday(tobeDeletedItem) }
//                    .show()
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
//        itemTouchHelper.attachToRecyclerView(rvSales)
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}