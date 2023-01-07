package com.example.fariyafardinfarhancollection

fun verifyDataFromUser(username: String, email: String, password: String, password2: String): Boolean{
    return (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) && (password == password2)
}

fun verifyLoginDataFromUser(email: String, password: String): Boolean{
    return (email.isNotEmpty() && password.isNotEmpty())
}

fun verifyProductCountDataFromUser(name: String, quantity: String, price: String): Boolean{
    return (name.isNotEmpty() && quantity.isNotEmpty() && price.isNotEmpty() )
}