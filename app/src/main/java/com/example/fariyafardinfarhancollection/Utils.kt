package com.example.fariyafardinfarhancollection

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

val EMPLOYEE_IMAGE_REQUEST_CODE = 0
val NID_IMAGE_REQUEST_CODE = 1

//FCM
const val BASE_URL = "https://fcm.googleapis.com/"
const val SERVER_KEY = "AAAAm-CtQeU:APA91bHXrF0vrKfNq_kbOalfHpUwvfn1KPziic1kuH3crJ0r8hBkb7iwF5rQrPdlqSnTDUQA1QDNuEuvZ3HbfOSN4UjCJDu9H0URcZYrG3D4EONXDZ81QSeTVS_byboOChFhChffxg4g"
const val CONTENT_TYPE = "application/json"
const val TOPIC = "/topics/myTopic"
const val CHANNEL_ID = "channelId"
const val CHANNEL_NAME = "channelName"



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
            (name.isNotEmpty() && phone.isNotEmpty())
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