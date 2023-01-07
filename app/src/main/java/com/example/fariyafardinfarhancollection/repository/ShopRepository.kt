package com.example.fariyafardinfarhancollection.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fariyafardinfarhancollection.dao.ShopDao
import com.example.fariyafardinfarhancollection.model.ProductCount

class ShopRepository(
    private val shopDao: ShopDao
) {

    fun getAllProductCount() = shopDao.getAllProductCount()
    suspend fun insertProductCount(productCount: ProductCount) = shopDao.insertProductCount(productCount)
    suspend fun updateProductCount(productCount: ProductCount) = shopDao.updateProductCount(productCount)
    suspend fun deleteProductCount(productCount: ProductCount) = shopDao.deleteProductCount(productCount)
}