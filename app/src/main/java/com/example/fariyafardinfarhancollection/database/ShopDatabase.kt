package com.example.fariyafardinfarhancollection.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fariyafardinfarhancollection.dao.ShopDao
import com.example.fariyafardinfarhancollection.model.*

@Database(
    entities = [
        ProductCount::class,
        WholesaleCount::class,
        SaleToday::class,
        OtherPaymentReceived::class,
        SpentToday::class,
        CustomerContact::class,
        StoreProduct::class,
        PublicPost::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: ShopDatabase? = null

        fun getDatabase(context: Context): ShopDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopDatabase::class.java,
                    "shop_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}