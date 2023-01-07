package com.example.fariyafardinfarhancollection.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fariyafardinfarhancollection.model.ProductCount

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
}