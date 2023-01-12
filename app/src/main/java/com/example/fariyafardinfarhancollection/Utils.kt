package com.example.fariyafardinfarhancollection

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

fun verifyDataFromUser(username: String, email: String, contact: String, password: String, password2: String): Boolean{
    return (username.isNotEmpty() && email.isNotEmpty() && contact.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) && (password == password2)
}

fun verifyLoginDataFromUser(email: String, password: String): Boolean{
    return (email.isNotEmpty() && password.isNotEmpty())
}

fun verifyProductCountDataFromUser(name: String, quantity: String, price: String): Boolean{
    return (name.isNotEmpty() && quantity.isNotEmpty() && price.isNotEmpty() )
}

fun verifyCustomerInformation(name: String, phone: String, address: String, due: String): Boolean{
    return (name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && due.isNotEmpty()) ||
            (name.isNotEmpty() && due.isNotEmpty()) || (phone.isNotEmpty() && due.isNotEmpty()) ||
            name.isNotEmpty() && phone.isNotEmpty()
}

fun verifyProductInformation(productName: String, quantityLeft: String, retailPrice: String, wholesalePrice: String): Boolean{
    return (productName.isNotEmpty() && quantityLeft.isNotEmpty() && retailPrice.isNotEmpty() && wholesalePrice.isNotEmpty()) ||
            (productName.isNotEmpty() && retailPrice.isNotEmpty()) || (productName.isNotEmpty() && quantityLeft.isNotEmpty()) ||
            productName.isNotEmpty() && wholesalePrice.isNotEmpty()
}

abstract class SwipeToDelete: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}