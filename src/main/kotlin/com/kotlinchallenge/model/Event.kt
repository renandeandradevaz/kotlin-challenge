package com.kotlinchallenge.model

import java.util.*

data class Event(
        val type: String,
        val employeeId: String,
        val employeeName: String,
        val awardId: String,
        val date: Date,
        var quantity: Float,
        var formattedQuantity: String? = ""
)