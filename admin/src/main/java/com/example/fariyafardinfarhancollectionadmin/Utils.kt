package com.example.fariyafardinfarhancollectionadmin

fun verifyLoginDataFromUser(email: String, password: String): Boolean{
    return (email.isNotEmpty() && password.isNotEmpty())
}