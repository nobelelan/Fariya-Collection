package com.example.fariyafardinfarhancollection.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.model.SaleToday
import com.example.fariyafardinfarhancollection.model.WholesaleCount
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

    init {
        shopRepository = ShopRepository(shopDao)
        getAllProductCount = shopRepository.getAllProductCount()
        getAllWholesaleCount = shopRepository.getAllWholesaleCount()
        getAllSaleToday = shopRepository.getAllSaleToday()
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
}