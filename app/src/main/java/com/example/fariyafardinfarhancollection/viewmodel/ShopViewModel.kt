package com.example.fariyafardinfarhancollection.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import kotlinx.coroutines.launch

class ShopViewModel(
    application: Application,
    var shopRepository: ShopRepository
):AndroidViewModel(application) {

    private val shopDao = ShopDatabase.getDatabase(application).shopDao()
    val getAllProductCount: LiveData<List<ProductCount>>

    init {
        shopRepository = ShopRepository(shopDao)
        getAllProductCount = shopRepository.getAllProductCount()
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
}