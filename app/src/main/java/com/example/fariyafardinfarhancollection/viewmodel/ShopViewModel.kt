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

    init {
        shopRepository = ShopRepository(shopDao)
        getAllProductCount = shopRepository.getAllProductCount()
        getAllWholesaleCount = shopRepository.getAllWholesaleCount()
        getAllSaleToday = shopRepository.getAllSaleToday()
        getAllOtherPaymentReceived = shopRepository.getAllOtherPaymentReceived()
        getAllSpentToday = shopRepository.getAllSpentToday()
        getAllCustomerContacts = shopRepository.getAllCustomerContacts()
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


    fun insertSaleToday(saleToday: SaleToday){
        viewModelScope.launch {
            shopRepository.insertSaleToday(saleToday)
        }
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
}