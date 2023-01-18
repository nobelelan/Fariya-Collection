package com.example.fariyafardinfarhancollection.ui.fragment.sales

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.SwipeToDelete
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.databinding.FragmentSaleBinding
import com.example.fariyafardinfarhancollection.model.*
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*


class SaleFragment : Fragment() {

    private var _binding: FragmentSaleBinding? = null
    private val binding get() = _binding!!

    private val salesProductCountAdapter by lazy { SalesProductCountAdapter() }
    private val salesWholesaleCountAdapter by lazy { SalesWholesaleCountAdapter() }
    private val otherPaymentReceivedAdapter by lazy { OtherPaymentReceivedAdapter() }
    private val spentTodayAdapter by lazy { SpentTodayAdapter() }

    private lateinit var shopViewModel: ShopViewModel

    private val counterCollectionRef = Firebase.firestore.collection("allCounters")
    private val saleTodayCollectionRef = Firebase.firestore.collection("saleTodays")
    private val productCountCollectionRef = Firebase.firestore.collection("productCounts")
    private val wholesaleCountCollectionRef = Firebase.firestore.collection("wholesaleCounts")
    private val otherPaymentCollectionRef = Firebase.firestore.collection("otherPayments")
    private val spentTodayCollectionRef = Firebase.firestore.collection("spentTodays")

    private var databaseSaleTodayCounter: Int? = null
    private var databaseProductCountCounter: Int? = null
    private var databaseWholesaleCountCounter: Int? = null
    private var databaseOtherPaymentCounter: Int? = null
    private var databaseSpentTodayCounter: Int? = null

    private lateinit var auth: FirebaseAuth
    private var currentEmployee: Employee? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sale, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaleBinding.bind(view)

        auth = Firebase.auth
        Firebase.firestore.collection("registeredEmployees").document(auth.currentUser!!.uid)
            .get().addOnSuccessListener {
            currentEmployee = it.toObject<Employee>()
        }

        val shopDao = ShopDatabase.getDatabase(requireContext()).shopDao()
        val shopRepository = ShopRepository(shopDao)
        val shopViewModelProviderFactory = ShopViewModelProviderFactory(requireActivity().application, shopRepository)
        shopViewModel = ViewModelProvider(this, shopViewModelProviderFactory)[ShopViewModel::class.java]

        setUpRetailSalesRecyclerView()
        setUpWholeSaleRecyclerView()
        setUpOtherPaymentRecyclerView()
        setUpSpentTodayRecyclerView()
        setUpCurrentDate()
        submitDataIntoSales()

//        shopViewModel.getAllProductCount.observe(viewLifecycleOwner, Observer {
//            if (it.isEmpty()){
//                for (i in 1..10){
//                    shopViewModel.insertProductCount(ProductCount(0))
//                }
//            }
//            salesProductCountAdapter.differ.submitList(it)
//        })
        setUpProductCount()

//        shopViewModel.getAllWholesaleCount.observe(viewLifecycleOwner, Observer {
//            if (it.isEmpty()){
//                for (i in 1..4){
//                    shopViewModel.insertWholesaleCount(WholesaleCount(0))
//                }
//            }else{
//                salesWholesaleCountAdapter.differ.submitList(it)
//            }
//        })
        setUpWholesaleCount()

//        shopViewModel.getAllOtherPaymentReceived.observe(viewLifecycleOwner, Observer {
//            if (it.isEmpty()){
//                for (i in 1..3){
//                    shopViewModel.insertOtherPaymentReceived(OtherPaymentReceived(0))
//                }
//            }else{
//                otherPaymentReceivedAdapter.differ.submitList(it)
//            }
//        })
        setUpOtherPaymentReceived()

//        shopViewModel.getAllSpentToday.observe(viewLifecycleOwner, Observer {
//            if (it.isEmpty()){
//                for (i in 1..3){
//                    shopViewModel.insertSpentToday(SpentToday(0))
//                }
//            }else{
//                spentTodayAdapter.differ.submitList(it)
//            }
//        })
        setUpSpentToday()

        salesProductCountAdapter.setUpdateItemListener(object : SalesProductCountAdapter.UpdateItemListener{
            override fun updateProductCount(id: Int, name: String, quantity: String, price: String, total: String) {
                productCountCollectionRef
                    .whereEqualTo("pcId", id)
//                    .whereEqualTo("name", name)
//                    .whereEqualTo("quantity", quantity)
//                    .whereEqualTo("price", price)
//                    .whereEqualTo("total", total)
                    .get()
                    .addOnSuccessListener { querySnapshot->
                        if (querySnapshot.documents.isNotEmpty()){
                            querySnapshot.forEach { documentSnapshot->
                                Firebase.firestore.runTransaction { transaction->
                                    val productCountRef = productCountCollectionRef.document(documentSnapshot.id)
                                    transaction.get(productCountRef)
                                    transaction.update(productCountRef, "name", name)
                                    transaction.update(productCountRef, "quantity", quantity)
                                    transaction.update(productCountRef, "price", price)
                                    transaction.update(productCountRef, "total", total)
                                    null
                                }.addOnSuccessListener {
//                                    shopViewModel.updateProductCount(ProductCount(id, name, quantity, price, total))
                                    Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener{
                                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            }
        })
        salesWholesaleCountAdapter.setUpdateItemListener(object : SalesWholesaleCountAdapter.UpdateItemListener{
            override fun updateProductCount(id: Int, name: String, quantity: String, price: String, total: String) {
                wholesaleCountCollectionRef
                    .whereEqualTo("wsId", id)
//                    .whereEqualTo("name", name)
//                    .whereEqualTo("quantity", quantity)
//                    .whereEqualTo("price", price)
//                    .whereEqualTo("total", total)
                    .get()
                    .addOnSuccessListener { querySnapshot->
                        if (querySnapshot.documents.isNotEmpty()){
                            querySnapshot.forEach { documentSnapshot->
                                Firebase.firestore.runTransaction { transaction->
                                    val wholesaleCountRef = wholesaleCountCollectionRef.document(documentSnapshot.id)
                                    transaction.get(wholesaleCountRef)
                                    transaction.update(wholesaleCountRef, "name", name)
                                    transaction.update(wholesaleCountRef, "quantity", quantity)
                                    transaction.update(wholesaleCountRef, "price", price)
                                    transaction.update(wholesaleCountRef, "total", total)
                                    null
                                }.addOnSuccessListener {
//                                    shopViewModel.updateWholesaleCount(WholesaleCount(id, name, quantity, price, total))
                                    Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener{
                                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            }
        })
        otherPaymentReceivedAdapter.setUpdateItemListener(object : OtherPaymentReceivedAdapter.UpdateItemListener{
            override fun updateProductCount(otherPaymentId: Int, senderName: String, paymentMethod: String, amount: String) {
                otherPaymentCollectionRef
                    .whereEqualTo("otherPaymentId", otherPaymentId)
//                    .whereEqualTo("senderName", senderName)
//                    .whereEqualTo("paymentMethod", paymentMethod)
//                    .whereEqualTo("amount", amount)
                    .get()
                    .addOnSuccessListener { querySnapshot->
                        if (querySnapshot.documents.isNotEmpty()){
                            querySnapshot.forEach { documentSnapshot->
                                Firebase.firestore.runTransaction { transaction->
                                    val otherPaymentRef = otherPaymentCollectionRef.document(documentSnapshot.id)
                                    transaction.get(otherPaymentRef)
                                    transaction.update(otherPaymentRef, "senderName", senderName)
                                    transaction.update(otherPaymentRef, "paymentMethod", paymentMethod)
                                    transaction.update(otherPaymentRef, "amount", amount)
                                    null
                                }.addOnSuccessListener {
//                                    shopViewModel.updateOtherPaymentReceived(OtherPaymentReceived(otherPaymentId, senderName, paymentMethod, amount))
                                    Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener{
                                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            }
        })
        spentTodayAdapter.setUpdateItemListener(object : SpentTodayAdapter.UpdateItemListener{
            override fun updateProductCount(spentTodayId: Int, reason: String, amount: String) {
                spentTodayCollectionRef
                    .whereEqualTo("spentTodayId", spentTodayId)
//                    .whereEqualTo("reason", reason)
//                    .whereEqualTo("amount", amount)
                    .get()
                    .addOnSuccessListener { querySnapshot->
                        if (querySnapshot.documents.isNotEmpty()){
                            querySnapshot.forEach { documentSnapshot->
                                Firebase.firestore.runTransaction { transaction->
                                    val spentTodayRef = spentTodayCollectionRef.document(documentSnapshot.id)
                                    transaction.get(spentTodayRef)
                                    transaction.update(spentTodayRef, "reason", reason)
                                    transaction.update(spentTodayRef, "amount", amount)
                                    null
                                }.addOnSuccessListener {
//                                    shopViewModel.updateSpentToday(SpentToday(spentTodayId, reason, amount))
                                    Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener{
                                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            }
        })

//        binding.txtRetailTotal.setOnClickListener {
//            setTotalRetailSale()
//        }
//        binding.txtWholesaleTotal.setOnClickListener {
//            setTotalWholesale()
//        }
//        binding.txtOtherPaymentTotal.setOnClickListener {
//            setTotalOtherPayment()
//        }
//        binding.txtSpentAmountTotal.setOnClickListener {
//            setTotalSpentToday()
//        }
        binding.txtRetailTotalAfterMinusSpentToday.setOnClickListener {
            if (binding.txtRetailTotal.text.toString() != " Total " && binding.txtSpentAmountTotal.text.toString() != " Total "){
                val spentAmount = binding.txtSpentAmountTotal.text.toString().toInt()
                val retailTotal = binding.txtRetailTotal.text.toString().toInt()
                binding.txtRetailTotalAfterMinusSpentToday.text = " ${retailTotal - spentAmount} "
            }else{
                Toast.makeText(requireContext(), "Calculate both totals first!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPreviousSalesReports.setOnClickListener {
            findNavController().navigate(R.id.action_saleFragment_to_recordsFragment)
        }

        binding.txtReset.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("This will remove all of your current data.")
            builder.setNegativeButton("Cancel"){_,_->}
            builder.setPositiveButton("Continue"){_,_->
                shopViewModel.apply {
                    Toast.makeText(requireContext(), "Reset Successful!", Toast.LENGTH_SHORT).show()
                    deleteAllProductCount()
                    deleteAllWholesaleCount()
                    deleteAllOtherPaymentReceived()
                    deleteAllSpentToday()
                    activity?.onBackPressed()
                }
            }
            builder.create().show()

        }

    }

    private fun setUpSpentToday(){
        spentTodayCollectionRef
            .orderBy("spentTodayId")
            .addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()){
                    val spentTodayList = querySnapshot.toObjects<SpentToday>()
                    spentTodayAdapter.differ.submitList(spentTodayList)
                    binding.txtSpentAmountTotal.setOnClickListener {
                        val strings =  arrayListOf<String>()
                        val spentTodayTotal =  arrayListOf<Int>()
                        spentTodayList.forEach { spentToday ->
                            spentToday.amount?.let { subTotal-> strings.add(subTotal) }
                        }
                        strings.forEach { each->
                            if (each.isNotEmpty() && each != "=" ){
                                spentTodayTotal.add(each.toInt())
                            }
                        }
                        binding.txtSpentAmountTotal.text = spentTodayTotal.sum().toString()
                    }
                }else{
                    insertSpentToday()
                }
            }
        }
    }

    private fun insertSpentToday(){
        Firebase.firestore.runTransaction { transaction->
            val counterRef = counterCollectionRef.document("spentTodayCounter")
            val counter = transaction.get(counterRef)
            val newCounter = counter["spentTodayId"] as Long + 1
            databaseSpentTodayCounter = newCounter.toInt()
            transaction.update(counterRef, "spentTodayId", newCounter)
            spentTodayCollectionRef.document().set(SpentToday(newCounter.toInt(), ))
            null
        }.addOnSuccessListener {
            Toast.makeText(requireContext(), "Item inserted!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewSpentAmount.isEnabled = true
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Insertion failed!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewSpentAmount.isEnabled = true
        }
    }

    private fun setUpOtherPaymentReceived(){
        otherPaymentCollectionRef
            .orderBy("otherPaymentId")
            .addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()){
                    val otherPaymentList = querySnapshot.toObjects<OtherPaymentReceived>()
                    otherPaymentReceivedAdapter.differ.submitList(otherPaymentList)
                    binding.txtOtherPaymentTotal.setOnClickListener {
                        val strings =  arrayListOf<String>()
                        val otherPaymentTotal =  arrayListOf<Int>()
                        otherPaymentList.forEach { otherPayment ->
                            otherPayment.amount?.let { subTotal-> strings.add(subTotal) }
                        }
                        strings.forEach { each->
                            if (each.isNotEmpty() && each != "=" ){
                                otherPaymentTotal.add(each.toInt())
                            }
                        }
                        binding.txtOtherPaymentTotal.text = otherPaymentTotal.sum().toString()
                    }
                }else{
                    insertOtherPayment()
                }
            }
        }
    }

    private fun insertOtherPayment(){
        Firebase.firestore.runTransaction { transaction->
            val counterRef = counterCollectionRef.document("otherPaymentCounter")
            val counter = transaction.get(counterRef)
            val newCounter = counter["otherPaymentId"] as Long + 1
            databaseOtherPaymentCounter = newCounter.toInt()
            transaction.update(counterRef, "otherPaymentId", newCounter)
            otherPaymentCollectionRef.document().set(OtherPaymentReceived(newCounter.toInt()))
            null
        }.addOnSuccessListener {
            Toast.makeText(requireContext(), "Item inserted!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewOtherPayment.isEnabled = true
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Insertion failed!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewOtherPayment.isEnabled = true
        }
    }

    private fun setUpWholesaleCount(){
        wholesaleCountCollectionRef
            .orderBy("wsId")
            .addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()){
                    val wholesaleCountList = querySnapshot.toObjects<WholesaleCount>()
                    salesWholesaleCountAdapter.differ.submitList(wholesaleCountList)
                    binding.txtWholesaleTotal.setOnClickListener {
                        val strings =  arrayListOf<String>()
                        val totalWholesale =  arrayListOf<Int>()
                        wholesaleCountList.forEach { wholesaleCount ->
                            wholesaleCount.total?.let { subTotal-> strings.add(subTotal) }
                        }
                        strings.forEach { each->
                            if (each.isNotEmpty() && each != "=" ){
                                totalWholesale.add(each.toInt())
                            }
                        }
                        binding.txtWholesaleTotal.text = totalWholesale.sum().toString()
                    }
                }else{
                    insertWholesaleCount()
                }
            }
        }
    }

    private fun insertWholesaleCount(){
        Firebase.firestore.runTransaction { transaction->
            val counterRef = counterCollectionRef.document("wholesaleCountCounter")
            val counter = transaction.get(counterRef)
            val newCounter = counter["wsId"] as Long + 1
            databaseWholesaleCountCounter = newCounter.toInt()
            transaction.update(counterRef, "wsId", newCounter)
            wholesaleCountCollectionRef.document().set(WholesaleCount(newCounter.toInt(), total = "="))
            null
        }.addOnSuccessListener {
            Toast.makeText(requireContext(), "Item inserted!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewWholesaleItem.isEnabled = true
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Insertion failed!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewWholesaleItem.isEnabled = true
        }
    }

    private fun setUpProductCount(){
        productCountCollectionRef
            .orderBy("pcId")
            .addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()){
                    val productCountList = querySnapshot.toObjects<ProductCount>()
                    salesProductCountAdapter.differ.submitList(productCountList)
                    binding.txtRetailTotal.setOnClickListener {
                        val strings =  arrayListOf<String>()
                        val totalRetailSale =  arrayListOf<Int>()
                        productCountList.forEach { productCount ->
                            productCount.total?.let { subTotal-> strings.add(subTotal) }
                        }
                        strings.forEach { each->
                            if (each.isNotEmpty() && each != "=" ){
                                totalRetailSale.add(each.toInt())
                            }
                        }
                        binding.txtRetailTotal.text = totalRetailSale.sum().toString()
                    }
                }else{
                    insertProductCount()
                }
            }
        }
    }

    private fun insertProductCount(){
        Firebase.firestore.runTransaction { transaction->
            val counterRef = counterCollectionRef.document("productCountCounter")
            val counter = transaction.get(counterRef)
            val newCounter = counter["pcId"] as Long + 1
            databaseProductCountCounter = newCounter.toInt()
            transaction.update(counterRef, "pcId", newCounter)
            productCountCollectionRef.document().set(ProductCount(newCounter.toInt(), total = "="))
            null
        }.addOnSuccessListener {
            Toast.makeText(requireContext(), "Item inserted!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewProductItem.isEnabled = true
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Insertion failed!", Toast.LENGTH_SHORT).show()
            binding.btnAddNewProductItem.isEnabled = true
        }
    }

//    @SuppressLint("SetTextI18n")
//    private fun setTotalSpentToday() {
//        val totalSpentToday = arrayListOf<Int>()
//        shopViewModel.getAllSpentToday.observe(viewLifecycleOwner, Observer {
//            it.forEach { spentToday ->
//                spentToday.amount?.let { amount -> totalSpentToday.add(amount.toInt()) }
//            }
//        })
//        binding.txtSpentAmountTotal.text = totalSpentToday.sum().toString()
//    }

//    @SuppressLint("SetTextI18n")
//    private fun setTotalOtherPayment() {
//        val totalOtherPaymentReceived = arrayListOf<Int>()
//        shopViewModel.getAllOtherPaymentReceived.observe(viewLifecycleOwner, Observer {
//            it.forEach { otherPaymentReceived ->
//                otherPaymentReceived.amount?.let { amount -> totalOtherPaymentReceived.add(amount.toInt()) }
//            }
//        })
//        binding.txtOtherPaymentTotal.text = totalOtherPaymentReceived.sum().toString()
//    }

    private fun setUpSpentTodayRecyclerView() {
        val rvSpentToday = binding.rvSpentAmount
        rvSpentToday.adapter = spentTodayAdapter
        rvSpentToday.layoutManager = LinearLayoutManager(requireContext())

        /*val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tobeDeletedItem = spentTodayAdapter.differ.currentList[viewHolder.adapterPosition]
                shopViewModel.deleteSpentToday(tobeDeletedItem)
                spentTodayAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(viewHolder.itemView, "Item Deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){ shopViewModel.insertSpentToday(tobeDeletedItem) }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvSpentToday)*/

        binding.btnAddNewSpentAmount.setOnClickListener {
            binding.btnAddNewSpentAmount.isEnabled = false
//            shopViewModel.insertSpentToday(SpentToday(0))
            insertSpentToday()
        }
    }

    private fun setUpOtherPaymentRecyclerView() {
        val rvOtherPayment = binding.rvOtherPayment
        rvOtherPayment.adapter = otherPaymentReceivedAdapter
        rvOtherPayment.layoutManager = LinearLayoutManager(requireContext())

        /*val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tobeDeletedItem = otherPaymentReceivedAdapter.differ.currentList[viewHolder.adapterPosition]
                shopViewModel.deleteOtherPaymentReceived(tobeDeletedItem)
                otherPaymentReceivedAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(viewHolder.itemView, "Item Deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){ shopViewModel.insertOtherPaymentReceived(tobeDeletedItem) }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvOtherPayment)*/

        binding.btnAddNewOtherPayment.setOnClickListener {
            binding.btnAddNewOtherPayment.isEnabled = false
//            shopViewModel.insertOtherPaymentReceived(OtherPaymentReceived(0))
            insertOtherPayment()
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

//    @SuppressLint("SetTextI18n")
//    private fun setTotalWholesale() {
//        val totalWholesale = arrayListOf<Int>()
//        shopViewModel.getAllWholesaleCount.observe(viewLifecycleOwner, Observer {
//            it.forEach { wholesaleCount ->
//                wholesaleCount.total?.let { subTotal -> totalWholesale.add(subTotal.toInt()) }
//            }
//        })
//        binding.txtWholesaleTotal.text = totalWholesale.sum().toString()
//    }


    private fun submitDataIntoSales() {

        binding.btnSubmitData.setOnClickListener {
            var retailSaleText = ""
            var otherPaymentText = ""
            var wholesaleText = ""
            var spentTodayText = ""

            binding.btnSubmitData.isEnabled = false
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm Submission")
            builder.setNegativeButton("Cancel"){_,_->
                binding.btnSubmitData.isEnabled = true
            }
            builder.setPositiveButton("Submit"){_,_->
                var retailSale = listOf<ProductCount>()
                var wholesale = listOf<WholesaleCount>()
                var otherPayment = listOf<OtherPaymentReceived>()
                var spentToday = listOf<SpentToday>()
//                shopViewModel.getAllProductCount.observe(viewLifecycleOwner, Observer {
//                    it?.let {
//                        retailSale = it
//                    }
//                })
                productCountCollectionRef.orderBy("pcId").get().addOnSuccessListener { querySnapshot->
                    querySnapshot?.let {
                        retailSale = it.toObjects<ProductCount>()
                    }
                }.addOnSuccessListener {
                    var productCountCounter = 1
                    retailSale.forEach {
                        retailSaleText += "$productCountCounter    ${it.name}    ${it.quantity}    *    ${it.price}    =    ${it.total}\n"
                        productCountCounter += 1
                    }
                }
//                shopViewModel.getAllWholesaleCount.observe(viewLifecycleOwner, Observer {
//                    it?.let {
//                        wholesale = it
//                    }
//                })
                wholesaleCountCollectionRef.orderBy("wsId").get().addOnSuccessListener { querySnapshot->
                    querySnapshot?.let {
                        wholesale = it.toObjects<WholesaleCount>()
                    }
                }.addOnSuccessListener {
                    var wholesaleCounter = 1
                    wholesale.forEach {
                        wholesaleText += "$wholesaleCounter    ${it.name}    ${it.quantity}    *    ${it.price}    =    ${it.total}\n"
                        wholesaleCounter += 1
                    }
                }
//                shopViewModel.getAllOtherPaymentReceived.observe(viewLifecycleOwner, Observer {
//                    it?.let {
//                        otherPayment = it
//                    }
//                })
                otherPaymentCollectionRef.orderBy("otherPaymentId").get().addOnSuccessListener { querySnapshot->
                    querySnapshot?.let {
                        otherPayment = it.toObjects<OtherPaymentReceived>()
                    }
                }.addOnSuccessListener {
                    var otherPaymentCounter = 1
                    otherPayment.forEach {
                        otherPaymentText += "$otherPaymentCounter    ${it.senderName}    (${it.paymentMethod})    =    ${it.amount}\n"
                        otherPaymentCounter += 1
                    }
                }
//                shopViewModel.getAllSpentToday.observe(viewLifecycleOwner, Observer {
//                    it?.let {
//                        spentToday = it
//                    }
//                })
                spentTodayCollectionRef.orderBy("spentTodayId").get().addOnSuccessListener { querySnapshot->
                    querySnapshot?.let {
                        spentToday = it.toObjects<SpentToday>()
                    }
                }.addOnSuccessListener {
                    var spentTodayCounter = 1
                    spentToday.forEach {
                        spentTodayText += "$spentTodayCounter    ${it.reason}    =    ${it.amount}\n"
                        spentTodayCounter += 1
                    }
                }

                Firebase.firestore.runTransaction { transaction->
                    val counterRef = counterCollectionRef.document("saleTodayCounter")
                    val counter = transaction.get(counterRef)
                    val newCounter = counter["saleId"] as Long + 1
                    databaseSaleTodayCounter = newCounter.toInt()
                    transaction.update(counterRef,"saleId", newCounter)

                    saleTodayCollectionRef.document().set(SaleToday(
                        saleId = newCounter.toInt(),
                        date = binding.txtCurrentDate.text.toString(),
                        retailSale = retailSaleText,
                        wholesale = wholesaleText,
                        wholesaleTotal = " = ${binding.txtWholesaleTotal.text} ",
                        retailTotal = " = ${binding.txtRetailTotal.text} ",
                        otherPayment = otherPaymentText,
                        spentToday = spentTodayText,
                        otherPaymentTotal = " = ${binding.txtOtherPaymentTotal.text} ",
                        spentTodayTotal = " = ${binding.txtSpentAmountTotal.text} ",
                        comment = " Comment: \n ${binding.edtComment.text}",
                        retailAfterSpentMinus = " Retail - Spent Money = ${binding.txtRetailTotalAfterMinusSpentToday.text}",
                        submittedBy = "${currentEmployee?.username}"
                    ))
                    null
                }.addOnSuccessListener {
                    /*shopViewModel.insertSaleToday(SaleToday(
                        saleId = 0,
                        date = binding.txtCurrentDate.text.toString(),
                        retailSale = retailSaleText,
                        wholesale = wholesaleText,
                        wholesaleTotal = " = ${binding.txtWholesaleTotal.text} ",
                        retailTotal = " = ${binding.txtRetailTotal.text} ",
                        otherPayment = otherPaymentText,
                        spentToday = spentTodayText,
                        otherPaymentTotal = " = ${binding.txtOtherPaymentTotal.text} ",
                        spentTodayTotal = " = ${binding.txtSpentAmountTotal.text} ",
                        comment = " Comment: \n ${binding.edtComment.text}",
                        retailAfterSpentMinus = " Retail - Spent Money = ${binding.txtRetailTotalAfterMinusSpentToday.text}"
                    ))*/
                    binding.btnSubmitData.isEnabled = true
                    Toast.makeText(requireContext(), "Record Saved Successfully!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    binding.btnSubmitData.isEnabled = true
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.create().show()
        }
    }

    private fun setUpWholeSaleRecyclerView() {
        val rvWholeSaleItems = binding.rvWholesaleItems
        rvWholeSaleItems.adapter = salesWholesaleCountAdapter
        rvWholeSaleItems.layoutManager = LinearLayoutManager(requireContext())

        /*val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tobeDeletedItem = salesWholesaleCountAdapter.differ.currentList[viewHolder.adapterPosition]
                shopViewModel.deleteWholesaleCount(tobeDeletedItem)
                salesWholesaleCountAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(viewHolder.itemView, "Item Deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){ shopViewModel.insertWholesaleCount(tobeDeletedItem) }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvWholeSaleItems)*/

        binding.btnAddNewWholesaleItem.setOnClickListener {
            binding.btnAddNewWholesaleItem.isEnabled = false
//            shopViewModel.insertWholesaleCount(WholesaleCount(0))
            insertWholesaleCount()
        }
    }

//    private fun setTotalRetailSale() {
//        val totalRetailSale = arrayListOf<Int>()
//        shopViewModel.getAllProductCount.observe(viewLifecycleOwner, Observer {
//            it.forEach { productCount ->
//                productCount.total?.let { subTotal -> totalRetailSale.add(subTotal.toInt()) }
//            }
//        })
//        binding.txtRetailTotal.text = totalRetailSale.sum().toString()
//    }

    private fun setUpRetailSalesRecyclerView() {
        val rvRetailItems = binding.rvRetailItems
        rvRetailItems.adapter = salesProductCountAdapter
        rvRetailItems.layoutManager = LinearLayoutManager(requireContext())

        /*val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tobeDeletedItem = salesProductCountAdapter.differ.currentList[viewHolder.adapterPosition]
                shopViewModel.deleteProductCount(tobeDeletedItem)
                salesProductCountAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(viewHolder.itemView, "Item Deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){ shopViewModel.insertProductCount(tobeDeletedItem) }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvRetailItems)*/

        binding.btnAddNewProductItem.setOnClickListener {
            binding.btnAddNewProductItem.isEnabled = false
//            shopViewModel.insertProductCount(ProductCount(0))
            insertProductCount()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}