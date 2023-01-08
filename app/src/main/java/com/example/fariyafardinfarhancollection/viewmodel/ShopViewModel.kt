package com.example.fariyafardinfarhancollection.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.model.ProductCount
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

    init {
        shopRepository = ShopRepository(shopDao)
        getAllProductCount = shopRepository.getAllProductCount()
        getAllWholesaleCount = shopRepository.getAllWholesaleCount()
    }
    fun insertProductCount(productCount: ProductCount){
        viewModelScope.launch {
            shopDao.insertProductCount(productCount)
        }
    }
    fun updateProductCount(productCount: ProductCount){
        viewModelScope.launch {
            shopDao.updateProductCount(productCount)
        }
    }
    fun deleteProductCount(productCount: ProductCount){
        viewModelScope.launch {
            shopDao.deleteProductCount(productCount)
        }
    }


    fun insertWholesaleCount(wholesaleCount: WholesaleCount){
        viewModelScope.launch {
            shopDao.insertWholesaleCount(wholesaleCount)
        }
    }
    fun updateWholesaleCount(wholesaleCount: WholesaleCount){
        viewModelScope.launch {
            shopDao.updateWholesaleCount(wholesaleCount)
        }
    }
    fun deleteWholesaleCount(wholesaleCount: WholesaleCount){
        viewModelScope.launch {
            shopDao.deleteWholesaleCount(wholesaleCount)
        }
    }
}