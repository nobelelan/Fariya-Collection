package com.example.fariyafardinfarhancollection.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fariyafardinfarhancollection.dao.ShopDao
import com.example.fariyafardinfarhancollection.model.*

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

    fun getAllOtherPaymentReceived() = shopDao.getAllOtherPaymentReceived()
    suspend fun insertOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived) = shopDao.insertOtherPaymentReceived(otherPaymentReceived)
    suspend fun updateOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived) = shopDao.updateOtherPaymentReceived(otherPaymentReceived)
    suspend fun deleteOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived) = shopDao.deleteOtherPaymentReceived(otherPaymentReceived)

    fun getAllSpentToday() = shopDao.getAllSpentToday()
    suspend fun insertSpentToday(spentToday: SpentToday) = shopDao.insertSpentToday(spentToday)
    suspend fun updateSpentToday(spentToday: SpentToday) = shopDao.updateSpentToday(spentToday)
    suspend fun deleteSpentToday(spentToday: SpentToday) = shopDao.deleteSpentToday(spentToday)
}