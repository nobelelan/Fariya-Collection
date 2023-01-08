package com.example.fariyafardinfarhancollection.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fariyafardinfarhancollection.dao.ShopDao
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.model.SaleToday
import com.example.fariyafardinfarhancollection.model.WholesaleCount

class ShopRepository(
    private val shopDao: ShopDao
) {

    fun getAllProductCount() = shopDao.getAllProductCount()
    suspend fun insertProductCount(productCount: ProductCount) = shopDao.insertProductCount(productCount)
    suspend fun updateProductCount(productCount: ProductCount) = shopDao.updateProductCount(productCount)
    suspend fun deleteProductCount(productCount: ProductCount) = shopDao.deleteProductCount(productCount)

    fun getAllWholesaleCount() = shopDao.getAllWholesaleCount()
    suspend fun insertWholesaleCount(wholesaleCount: WholesaleCount) = shopDao.insertWholesaleCount(wholesaleCount)
    suspend fun updateWholesaleCount(wholesaleCount: WholesaleCount) = shopDao.updateWholesaleCount(wholesaleCount)
    suspend fun deleteWholesaleCount(wholesaleCount: WholesaleCount) = shopDao.deleteWholesaleCount(wholesaleCount)


    fun getAllSaleToday() = shopDao.getAllSaleToday()
    suspend fun insertSaleToday(saleToday: SaleToday) = shopDao.insertSaleToday(saleToday)
}