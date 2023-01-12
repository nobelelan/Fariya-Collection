package com.example.fariyafardinfarhancollection.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fariyafardinfarhancollection.model.*

@Dao
interface ShopDao {

    @Query("SELECT * FROM product_count_table")
    fun getAllProductCount(): LiveData<List<ProductCount>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProductCount(productCount: ProductCount)
    @Update
    suspend fun updateProductCount(productCount: ProductCount)
    @Delete
    suspend fun deleteProductCount(productCount: ProductCount)
    @Query("DELETE FROM product_count_table")
    suspend fun deleteAllProductCount()


    @Query("SELECT * FROM wholesale_count_table")
    fun getAllWholesaleCount(): LiveData<List<WholesaleCount>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWholesaleCount(wholesaleCount: WholesaleCount)
    @Update
    suspend fun updateWholesaleCount(wholesaleCount: WholesaleCount)
    @Delete
    suspend fun deleteWholesaleCount(wholesaleCount: WholesaleCount)
    @Query("DELETE FROM wholesale_count_table")
    suspend fun deleteAllWholesaleCount()

    @Query("SELECT * FROM sale_today_table ORDER BY saleId DESC")
    fun getAllSaleToday():LiveData<List<SaleToday>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSaleToday(saleToday: SaleToday)
    @Delete
    suspend fun deleteSaleToday(saleToday: SaleToday)

    @Query("SELECT * FROM other_payment_received_table")
    fun getAllOtherPaymentReceived(): LiveData<List<OtherPaymentReceived>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived)
    @Update
    suspend fun updateOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived)
    @Delete
    suspend fun deleteOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived)
    @Query("DELETE FROM other_payment_received_table")
    suspend fun deleteAllOtherPaymentReceived()

    @Query("SELECT * FROM spent_today_table")
    fun getAllSpentToday(): LiveData<List<SpentToday>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpentToday(SpentToday: SpentToday)
    @Update
    suspend fun updateSpentToday(spentToday: SpentToday)
    @Delete
    suspend fun deleteSpentToday(spentToday: SpentToday)
    @Query("DELETE FROM spent_today_table")
    suspend fun deleteAllSpentToday()

    @Query("SELECT * FROM customer_contact_table ORDER BY ccId DESC")
    fun getAllCustomerContacts(): LiveData<List<CustomerContact>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerContact(customerContact: CustomerContact)
    @Update
    suspend fun updateCustomerContact(customerContact: CustomerContact)
    @Delete
    suspend fun deleteCustomerContact(customerContact: CustomerContact)

    @Query("SELECT * FROM store_product_table ORDER BY storeProductId DESC")
    fun getAllStoreProduct(): LiveData<List<StoreProduct>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoreProduct(storeProduct: StoreProduct)
    @Update
    suspend fun updateStoreProduct(storeProduct: StoreProduct)
    @Delete
    suspend fun deleteStoreProduct(storeProduct: StoreProduct)

    @Query("SELECT * FROM public_post_table ORDER BY publicPostId DESC")
    fun getAllPublicPost(): LiveData<List<PublicPost>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublicPost(publicPost: PublicPost)
    @Update
    suspend fun updatePublicPost(publicPost: PublicPost)
    @Delete
    suspend fun deletePublicPost(publicPost: PublicPost)
}