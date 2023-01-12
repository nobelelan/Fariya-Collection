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
    suspend fun deleteAllProductCount() = shopDao.deleteAllProductCount()

    fun getAllWholesaleCount() = shopDao.getAllWholesaleCount()
    suspend fun insertWholesaleCount(wholesaleCount: WholesaleCount) = shopDao.insertWholesaleCount(wholesaleCount)
    suspend fun updateWholesaleCount(wholesaleCount: WholesaleCount) = shopDao.updateWholesaleCount(wholesaleCount)
    suspend fun deleteWholesaleCount(wholesaleCount: WholesaleCount) = shopDao.deleteWholesaleCount(wholesaleCount)
    suspend fun deleteAllWholesaleCount() = shopDao.deleteAllWholesaleCount()


    fun getAllSaleToday() = shopDao.getAllSaleToday()
    suspend fun insertSaleToday(saleToday: SaleToday) = shopDao.insertSaleToday(saleToday)
    suspend fun deleteSaleToday(saleToday: SaleToday) = shopDao.deleteSaleToday(saleToday)

    fun getAllOtherPaymentReceived() = shopDao.getAllOtherPaymentReceived()
    suspend fun insertOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived) = shopDao.insertOtherPaymentReceived(otherPaymentReceived)
    suspend fun updateOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived) = shopDao.updateOtherPaymentReceived(otherPaymentReceived)
    suspend fun deleteOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived) = shopDao.deleteOtherPaymentReceived(otherPaymentReceived)
    suspend fun deleteAllOtherPaymentReceived() = shopDao.deleteAllOtherPaymentReceived()

    fun getAllSpentToday() = shopDao.getAllSpentToday()
    suspend fun insertSpentToday(spentToday: SpentToday) = shopDao.insertSpentToday(spentToday)
    suspend fun updateSpentToday(spentToday: SpentToday) = shopDao.updateSpentToday(spentToday)
    suspend fun deleteSpentToday(spentToday: SpentToday) = shopDao.deleteSpentToday(spentToday)
    suspend fun deleteAllSpentToday() = shopDao.deleteAllSpentToday()

    fun getAllCustomerContacts() = shopDao.getAllCustomerContacts()
    suspend fun insertCustomerContact(customerContact: CustomerContact) = shopDao.insertCustomerContact(customerContact)
    suspend fun updateCustomerContact(customerContact: CustomerContact) = shopDao.updateCustomerContact(customerContact)
    suspend fun deleteCustomerContact(customerContact: CustomerContact) = shopDao.deleteCustomerContact(customerContact)

    fun getAllStoreProduct() = shopDao.getAllStoreProduct()
    suspend fun insertStoreProduct(storeProduct: StoreProduct) = shopDao.insertStoreProduct(storeProduct)
    suspend fun updateStoreProduct(storeProduct: StoreProduct) = shopDao.updateStoreProduct(storeProduct)
    suspend fun deleteStoreProduct(storeProduct: StoreProduct) = shopDao.deleteStoreProduct(storeProduct)

    fun getAllPublicPost() = shopDao.getAllPublicPost()
    suspend fun insertPublicPost(publicPost: PublicPost) = shopDao.insertPublicPost(publicPost)
    suspend fun updatePublicPost(publicPost: PublicPost) = shopDao.updatePublicPost(publicPost)
    suspend fun deletePublicPost(publicPost: PublicPost) = shopDao.deletePublicPost(publicPost)
}