package com.example.fariyafardinfarhancollection.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fariyafardinfarhancollection.repository.ShopRepository

class ShopViewModelProviderFactory(
    private val application: Application,
    private val shopRepository: ShopRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShopViewModel(application, shopRepository) as T
    }
}