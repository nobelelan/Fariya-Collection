package com.example.fariyafardinfarhancollectionadmin.model

data class SaleRecords(
    var saleRecordId: Int = 0,
    var date: String? = "",
    var retailSale: String? = "",
    var wholesale: String? = "",
    var otherPayment: String? = "",
    var spentToday: String? = "",
    var retailTotal: String? = "",
    var wholesaleTotal: String? = "",
    var otherPaymentTotal: String? = "",
    var spentTodayTotal: String? = "",
    var comment: String? = "",
    var retailAfterSpentMinus: String? = "",
    var submittedBy: String? = ""
)
