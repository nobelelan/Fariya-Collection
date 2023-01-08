package com.example.fariyafardinfarhancollection.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fariyafardinfarhancollection.model.ProductCount
import com.example.fariyafardinfarhancollection.model.WholesaleCount

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
}