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


    @Query("SELECT * FROM wholesale_count_table")
    fun getAllWholesaleCount(): LiveData<List<WholesaleCount>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWholesaleCount(wholesaleCount: WholesaleCount)
    @Update
    suspend fun updateWholesaleCount(wholesaleCount: WholesaleCount)
    @Delete
    suspend fun deleteWholesaleCount(wholesaleCount: WholesaleCount)


    @Query("SELECT * FROM sale_today_table ORDER BY saleId DESC")
    fun getAllSaleToday():LiveData<List<SaleToday>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSaleToday(saleToday: SaleToday)

    @Query("SELECT * FROM other_payment_received_table")
    fun getAllOtherPaymentReceived(): LiveData<List<OtherPaymentReceived>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived)
    @Update
    suspend fun updateOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived)
    @Delete
    suspend fun deleteOtherPaymentReceived(otherPaymentReceived: OtherPaymentReceived)

    @Query("SELECT * FROM spent_today_table")
    fun getAllSpentToday(): LiveData<List<SpentToday>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpentToday(SpentToday: SpentToday)
    @Update
    suspend fun updateSpentToday(spentToday: SpentToday)
    @Delete
    suspend fun deleteSpentToday(spentToday: SpentToday)
}