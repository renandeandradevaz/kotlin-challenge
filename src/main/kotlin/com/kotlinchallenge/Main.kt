package com.kotlinchallenge

import com.kotlinchallenge.service.EventService
import com.kotlinchallenge.utils.Constants
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    if (args.size < 2) {
        throw IllegalArgumentException("You must pass the file and the date as parameters")
    }

    println()
    println("Starting application")
    println()

    val precisionDigits = if(args.size >= 3) args[2] else "0"

    val timeInMillis = measureTimeMillis {
        EventService().processEvents(args[0], args[1], precisionDigits).forEach {
            println("${it.employeeId},${it.employeeName},${it.awardId},${it.formattedQuantity}")
        }
    }

    println()
    println("(The operation took $timeInMillis ms)")
    println()
}

