package com.example.fariyafardinfarhancollection.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.model.*
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import kotlinx.coroutines.launch

class ShopViewModel(
    application: Application,
    var shopRepository: ShopRepository
):AndroidViewModel(application) {

    private val shopDao = ShopDatabase.getDatabase(application).shopDao()
    val getAllProductCount: LiveData<List<ProductCount>>
    val getAllWholesaleCount: LiveData<List<WholesaleCount>>
    val getAllSaleToday: LiveData<List<SaleToday>>
    val getAllOtherPaymentReceived: LiveData<List<OtherPaymentReceived>>
    val getAllSpentToday: LiveData<List<SpentToday>>
    val getAllCustomerContacts: LiveData<List<CustomerContact>>
    val getAllStoreProduct: LiveData<List<StoreProduct>>
    val getAllPublicPost: LiveData<List<PublicPost>>

    init {
        shopRepository = ShopRepository(shopDao)
        getAllProductCount = shopRepository.getAllProductCount()
        getAllWholesaleCount = shopRepository.getAllWholesaleCount()
        getAllSaleToday = shopRepository.getAllSaleToday()
        getAllOtherPaymentReceived = shopRepository.getAllOtherPaymentReceived()
        getAllSpentToday = shopRepository.getAllSpentToday()
        getAllCustomerContacts = shopRepository.getAllCustomerContacts()
        getAllStoreProduct = shopRepository.getAllStoreProduct()
        getAllPublicPost = shopRepository.getAllPublicPost()
    }
    fun insertProductCount(productCount: ProductCount){
        viewModelScope.launch {
            shopRepository.insertProductCount(productCount)
        }
    }
    fun updateProductCount(productCount: ProductCount){
        viewModelScope.launch {
            shopRepository.updateProductCount(productCount)
        }
    }
    fun deleteProductCount(productCount: ProductCount){
        viewModelScope.launch {
            shopRepository.deleteProductCount(productCount)
        }
    }
    fun deleteAllProductCount(){
        viewModelScope.launch {
            shopRepository.deleteAllProductCount()
        }
    }


    fun insertWholesaleCount(wholesaleCount: WholesaleCount){
        viewModelScope.launch {
            shopRepository.insertWholesaleCount(wholesaleCount)
        }
    }
    fun updateWholesaleCount(wholesaleCount: WholesaleCount){
        viewModelScope.launch {
            shopRepository.updateWholesaleCount(wholesaleCount)
        }
    }
    fun deleteWholesaleCount(wholesaleCount: WholesaleCount){
        viewModelScope.launch {
            shopRepository.deleteWholesaleCount(wholesaleCount)
        }
    }
    fun deleteAllWholesaleCount(){
        viewModelScope.launch {
            shopRepository.deleteAllWholesaleCount()
        }
    }


    fun insertSaleToday(saleToday: SaleToday){
        viewModelScope.launch {
            shopRepository.insertSaleToday(saleToday)
        }
    }
    fun deleteSaleToday(saleToday: SaleToday){
        viewModelScope.launch {
            shopRepository.deleteSaleToday(saleToday)
        }
    }
    fun searchSaleToday(saleTodayQuery: String): LiveData<List<SaleToday>>{
        return shopRepository.searchSaleToday(saleTodayQuery)
    }


    fun insertOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived){
        viewModelScope.launch {
            shopRepository.insertOtherPaymentReceived(otherPaymentReceived)
        }
    }
    fun updateOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived){
        viewModelScope.launch {
            shopRepository.updateOtherPaymentReceived(otherPaymentReceived)
        }
    }
    fun deleteOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived){
        viewModelScope.launch {
            shopRepository.deleteOtherPaymentReceived(otherPaymentReceived)
        }
    }
    fun deleteAllOtherPaymentReceived(){
        viewModelScope.launch {
            shopRepository.deleteAllOtherPaymentReceived()
        }
    }


    fun insertSpentToday(spentToday: SpentToday){
        viewModelScope.launch {
            shopRepository.insertSpentToday(spentToday)
        }
    }
    fun updateSpentToday(spentToday: SpentToday){
        viewModelScope.launch {
            shopRepository.updateSpentToday(spentToday)
        }
    }
    fun deleteSpentToday(spentToday: SpentToday){
        viewModelScope.launch {
            shopRepository.deleteSpentToday(spentToday)
        }
    }
    fun deleteAllSpentToday(){
        viewModelScope.launch {
            shopRepository.deleteAllSpentToday()
        }
    }

    fun insertCustomerContact(customerContact: CustomerContact){
        viewModelScope.launch {
            shopRepository.insertCustomerContact(customerContact)
        }
    }
    fun updateCustomerContact(customerContact: CustomerContact){
        viewModelScope.launch {
            shopRepository.updateCustomerContact(customerContact)
        }
    }
    fun deleteCustomerContact(customerContact: CustomerContact){
        viewModelScope.launch {
            shopRepository.deleteCustomerContact(customerContact)
        }
    }
    fun searchCustomerContact(customerContactQuery: String): LiveData<List<CustomerContact>>{
        return shopRepository.searchCustomerContact(customerContactQuery)
    }

    fun insertStoreProduct(storeProduct: StoreProduct){
        viewModelScope.launch {
            shopRepository.insertStoreProduct(storeProduct)
        }
    }
    fun updateStoreProduct(storeProduct: StoreProduct){
        viewModelScope.launch {
            shopRepository.updateStoreProduct(storeProduct)
        }
    }
    fun deleteStoreProduct(storeProduct: StoreProduct){
        viewModelScope.launch {
            shopRepository.deleteStoreProduct(storeProduct)
        }
    }
    fun searchStoreProduct(storeProductQuery: String): LiveData<List<StoreProduct>>{
        return shopRepository.searchStoreProduct(storeProductQuery)
    }

    fun insertPublicPost(publicPost: PublicPost){
        viewModelScope.launch {
            shopRepository.insertPublicPost(publicPost)
        }
    }
    fun updatePublicPost(publicPost: PublicPost){
        viewModelScope.launch {
            shopRepository.updatePublicPost(publicPost)
        }
    }
    fun deletePublicPost(publicPost: PublicPost){
        viewModelScope.launch {
            shopRepository.deletePublicPost(publicPost)
        }
    }
}