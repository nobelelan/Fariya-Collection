package com.example.fariyafardinfarhancollection.ui.fragment.sales

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
import com.example.fariyafardinfarhancollection.model.*
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory
import com.google.common.cache.Cache
import java.text.SimpleDateFormat
import java.util.*


class SaleFragment : Fragment() {

    private var _binding: FragmentSaleBinding? = null
    private val binding get() = _binding!!

    private val salesProductCountAdapter by lazy { SalesProductCountAdapter() }
    private val salesWholesaleCountAdapter by lazy { SalesWholesaleCountAdapter() }
    private val salesAdapter by lazy { SalesAdapter() }
    private val otherPaymentReceivedAdapter by lazy { OtherPaymentReceivedAdapter() }
    private val spentTodayAdapter by lazy { SpentTodayAdapter() }

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

        setUpRetailSalesRecyclerView()
        setUpWholeSaleRecyclerView()
        setUpSalesRecyclerView()
        setUpOtherPaymentRecyclerView()
        setUpSpentTodayRecyclerView()
        setUpCurrentDate()

        shopViewModel.getAllProductCount.observe(viewLifecycleOwner, Observer {
            salesProductCountAdapter.differ.submitList(it)
        })
        shopViewModel.getAllWholesaleCount.observe(viewLifecycleOwner, Observer {
            salesWholesaleCountAdapter.differ.submitList(it)
        })
        shopViewModel.getAllSaleToday.observe(viewLifecycleOwner, Observer {
            salesAdapter.differ.submitList(it)
        })
        shopViewModel.getAllOtherPaymentReceived.observe(viewLifecycleOwner, Observer {
            otherPaymentReceivedAdapter.differ.submitList(it)
        })
        shopViewModel.getAllSpentToday.observe(viewLifecycleOwner, Observer {
            spentTodayAdapter.differ.submitList(it)
        })

        salesProductCountAdapter.setUpdateItemListener(object : SalesProductCountAdapter.UpdateItemListener{
            override fun updateProductCount(id: Int, name: String, quantity: String, price: String, total: String) {
                shopViewModel.updateProductCount(ProductCount(id, name, quantity, price, total))
            }
        })
        salesWholesaleCountAdapter.setUpdateItemListener(object : SalesWholesaleCountAdapter.UpdateItemListener{
            override fun updateProductCount(id: Int, name: String, quantity: String, price: String, total: String) {
                shopViewModel.updateWholesaleCount(WholesaleCount(id, name, quantity, price, total))
            }
        })
        otherPaymentReceivedAdapter.setUpdateItemListener(object : OtherPaymentReceivedAdapter.UpdateItemListener{
            override fun updateProductCount(otherPaymentId: Int, senderName: String, paymentMethod: String, amount: String) {
                shopViewModel.updateOtherPaymentReceived(OtherPaymentReceived(otherPaymentId, senderName, paymentMethod, amount))
            }
        })
        spentTodayAdapter.setUpdateItemListener(object : SpentTodayAdapter.UpdateItemListener{
            override fun updateProductCount(spentTodayId: Int, reason: String, amount: String) {
                shopViewModel.updateSpentToday(SpentToday(spentTodayId, reason, amount))
            }
        })

        binding.txtRetailTotal.setOnClickListener {
            setTotalRetailSale()
        }
        binding.txtWholesaleTotal.setOnClickListener {
            setTotalWholesale()
        }
        binding.txtOtherPaymentTotal.setOnClickListener {
            setTotalOtherPayment()
        }
        binding.txtSpentAmountTotal.setOnClickListener {
            setTotalSpentToday()
        }

    }

    private fun setTotalSpentToday() {
        val totalSpentToday = arrayListOf<Int>()
        shopViewModel.getAllSpentToday.observe(viewLifecycleOwner, Observer {
            it.forEach { spentToday ->
                spentToday.amount?.let { amount -> totalSpentToday.add(amount.toInt()) }
            }
        })
        binding.txtSpentAmountTotal.text = totalSpentToday.sum().toString()
    }

    private fun setTotalOtherPayment() {
        val totalOtherPaymentReceived = arrayListOf<Int>()
        shopViewModel.getAllOtherPaymentReceived.observe(viewLifecycleOwner, Observer {
            it.forEach { otherPaymentReceived ->
                otherPaymentReceived.amount?.let { amount -> totalOtherPaymentReceived.add(amount.toInt()) }
            }
        })
        binding.txtOtherPaymentTotal.text = totalOtherPaymentReceived.sum().toString()
    }

    private fun setUpSpentTodayRecyclerView() {
        val rvSpentToday = binding.rvSpentAmount
        rvSpentToday.adapter = spentTodayAdapter
        rvSpentToday.layoutManager = LinearLayoutManager(requireContext())

        val spentToday1 = SpentToday(1)
        val spentToday2 = SpentToday(2)
        val spentToday3 = SpentToday(3)

        val emptySpentToday = arrayListOf(spentToday1, spentToday2, spentToday3)
        emptySpentToday.forEach{
            shopViewModel.insertSpentToday(it)
        }
        binding.btnAddNewSpentAmount.setOnClickListener {
            shopViewModel.insertSpentToday(SpentToday(0))
        }
    }

    private fun setUpOtherPaymentRecyclerView() {
        val rvOtherPayment = binding.rvOtherPayment
        rvOtherPayment.adapter = otherPaymentReceivedAdapter
        rvOtherPayment.layoutManager = LinearLayoutManager(requireContext())

        val otherPaymentReceived1 = OtherPaymentReceived(1)
        val otherPaymentReceived2 = OtherPaymentReceived(2)
        val otherPaymentReceived3 = OtherPaymentReceived(3)

        val emptyOtherPaymentReceived = arrayListOf(otherPaymentReceived1, otherPaymentReceived2, otherPaymentReceived3)
        emptyOtherPaymentReceived.forEach{
            shopViewModel.insertOtherPaymentReceived(it)
        }
        binding.btnAddNewOtherPayment.setOnClickListener {
            shopViewModel.insertOtherPaymentReceived(OtherPaymentReceived(0))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpCurrentDate() {
        val sharedPref = this.activity?.getSharedPreferences("datePicker", MODE_PRIVATE)
        val editor = sharedPref?.edit()

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        editor?.putString("currentDate"," $day/${month + 1}/$year ")

        val currentDate = sharedPref?.getString("currentDate", "Date update required")
        val selectedDate = sharedPref?.getString("selectedDate", "Date update required")
        if (currentDate != selectedDate){
            binding.txtCurrentDate.text = selectedDate
        }else{
            binding.txtCurrentDate.text = " $day/${month + 1}/$year "
        }

        binding.txtCurrentDateUpdate.setOnClickListener {
            DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
                binding.txtCurrentDate.text = " $mDay/${mMonth + 1}/$mYear "
                editor?.putString("selectedDate", " $mDay/${mMonth + 1}/$mYear ")
                editor?.apply()
            },year, month, day).show()
        }
    }

    private fun setTotalWholesale() {
        val totalWholesale = arrayListOf<Int>()
        shopViewModel.getAllWholesaleCount.observe(viewLifecycleOwner, Observer {
            it.forEach { wholesaleCount ->
                wholesaleCount.total?.let { subTotal -> totalWholesale.add(subTotal.toInt()) }
            }
        })
        binding.txtWholesaleTotal.text = totalWholesale.sum().toString()
    }


    private fun setUpSalesRecyclerView() {
        val rvSales = binding.rvSales
        rvSales.adapter = salesAdapter
        rvSales.layoutManager = LinearLayoutManager(requireContext())

        binding.btnSubmitData.setOnClickListener {
            var retailSale = listOf<ProductCount>()
            var wholesale = listOf<WholesaleCount>()
            shopViewModel.getAllProductCount.observe(viewLifecycleOwner, Observer {
                it?.let {
                    retailSale = it
                }
            })
            shopViewModel.getAllWholesaleCount.observe(viewLifecycleOwner, Observer {
                it?.let {
                    wholesale = it
                }
            })
            var retailSaleText = ""
            retailSale.forEach {
                retailSaleText += "${it.pcId}    ${it.name}    ${it.quantity}    *    ${it.price}    =    ${it.total}\n"
            }
            var wholesaleText = ""
            wholesale.forEach {
                wholesaleText += "${it.wsId}    ${it.name}    ${it.quantity}    *    ${it.price}    =    ${it.total}\n"
            }
            shopViewModel.insertSaleToday(SaleToday(
                saleId = 0,
                date = binding.txtCurrentDate.text.toString(),
                retailSale = retailSaleText,
                wholesale = wholesaleText,
                wholesaleTotal = " Wholesale Total = ${binding.txtWholesaleTotal.text} ",
                retailTotal = " Retail Total = ${binding.txtRetailTotal.text} "
            ))
        }
    }

    private fun setUpWholeSaleRecyclerView() {
        val rvWholeSaleItems = binding.rvWholesaleItems
        rvWholeSaleItems.adapter = salesWholesaleCountAdapter
        rvWholeSaleItems.layoutManager = LinearLayoutManager(requireContext())

        val wholesaleCount1 = WholesaleCount(1)
        val wholesaleCount2 = WholesaleCount(2)
        val wholesaleCount3 = WholesaleCount(3)
        val wholesaleCount4 = WholesaleCount(4)
        val wholesaleCount5 = WholesaleCount(5)

        val emptyProductCount = arrayListOf(wholesaleCount1, wholesaleCount2, wholesaleCount3, wholesaleCount4, wholesaleCount5)
        emptyProductCount.forEach{
            shopViewModel.insertWholesaleCount(it)
        }
        binding.btnAddNewWholesaleItem.setOnClickListener {
            shopViewModel.insertWholesaleCount(WholesaleCount(0))
        }
    }

    private fun setTotalRetailSale() {
        val totalRetailSale = arrayListOf<Int>()
        shopViewModel.getAllProductCount.observe(viewLifecycleOwner, Observer {
            it.forEach { productCount ->
                productCount.total?.let { subTotal -> totalRetailSale.add(subTotal.toInt()) }
            }
        })
        binding.txtRetailTotal.text = totalRetailSale.sum().toString()
    }

    private fun setUpRetailSalesRecyclerView() {
        val rvRetailItems = binding.rvRetailItems
        rvRetailItems.adapter = salesProductCountAdapter
        rvRetailItems.layoutManager = LinearLayoutManager(requireContext())

        val productCount1 = ProductCount(1)
        val productCount2 = ProductCount(2)
        val productCount3 = ProductCount(3)
        val productCount4 = ProductCount(4)
        val productCount5 = ProductCount(5)
        val productCount6 = ProductCount(6)
        val productCount7 = ProductCount(7)
        val productCount8 = ProductCount(8)
        val productCount9 = ProductCount(9)
        val productCount10 = ProductCount(10)

        val emptyProductCount = arrayListOf(productCount1, productCount2, productCount3, productCount4, productCount5,
            productCount6, productCount7, productCount8, productCount9, productCount10)

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