package com.example.fariyafardinfarhancollection.ui.fragment.sales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.databinding.FragmentSaleBinding
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory


class SaleFragment : Fragment() {

    private var _binding: FragmentSaleBinding? = null
    private val binding get() = _binding!!

    private val salesProductCountAdapter by lazy { SalesProductCountAdapter() }
    private lateinit var shopViewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaleBinding.bind(view)

        val shopDao = ShopDatabase.getDatabase(requireContext()).shopDao()
        val shopRepository = ShopRepository(shopDao)
        val shopViewModelProviderFactory = ShopViewModelProviderFactory(requireActivity().application, shopRepository)
        shopViewModel = ViewModelProvider(this, shopViewModelProviderFactory)[ShopViewModel::class.java]

        setUpProductCountRecyclerView()

        shopViewModel.getAllProductCount.observe(viewLifecycleOwner, Observer {
            salesProductCountAdapter.differ.submitList(it)
        })

        salesProductCountAdapter.setUpdateItemListener(object : SalesProductCountAdapter.UpdateItemListener{
            override fun updateProductCount(id: Int, name: String, quantity: String, price: String, total: String) {
                val toUpdate = ProductCount(id, name, quantity, price, total)
                Toast.makeText(requireContext(), "$id,$name,$quantity,$price,$total", Toast.LENGTH_LONG).show()
                shopViewModel.updateProductCount(toUpdate)
            }
        })

    }

    private fun setUpProductCountRecyclerView() {
        val rvRetailItems = binding.rvRetailItems
        rvRetailItems.adapter = salesProductCountAdapter
        rvRetailItems.layoutManager = LinearLayoutManager(requireContext())

        val productCount1 = ProductCount(1)
        val productCount2 = ProductCount(1)
        val productCount3 = ProductCount(1)
        val productCount4 = ProductCount(1)
        val productCount5 = ProductCount(5)
        val productCount6 = ProductCount(6)
        val productCount7 = ProductCount(7)
        val productCount8 = ProductCount(8)
        val productCount9 = ProductCount(9)
        val productCount10 = ProductCount(10)
        val productCount11 = ProductCount(11)
        val productCount12 = ProductCount(12)

        val emptyProductCount = arrayListOf(productCount1, productCount2, productCount3, productCount4, productCount5,
            productCount6, productCount7, productCount8, productCount9, productCount10, productCount11, productCount12)

        emptyProductCount.forEach{
            shopViewModel.insertProductCount(it)
        }

        binding.btnAddNewProductItem.setOnClickListener {
            shopViewModel.insertProductCount(ProductCount(0))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}