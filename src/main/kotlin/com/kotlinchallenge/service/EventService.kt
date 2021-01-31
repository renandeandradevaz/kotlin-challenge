package com.kotlinchallenge.service

import com.kotlinchallenge.model.Event
import com.kotlinchallenge.utils.Constants
import com.kotlinchallenge.utils.Constants.MAX_PRECISION_DIGITS
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class EventService {

    fun processEvents(fileName: String, dateArgString: String, precisionDigitsString: String = "0"): ArrayList<Event> {
        val dateArg = SimpleDateFormat("yyyy-MM-dd").parse(dateArgString)
        val result = TreeMap<String, Event>()
        readCSV(fileName, dateArg, result)
        result.values.removeIf { event -> event.quantity < 0 }
        formatQuantity(precisionDigitsString, result)
        return ArrayList(result.values)
    }

    private fun formatQuantity(precisionDigitsString: String, result: TreeMap<String, Event>) {

        val precisionDigits = if (precisionDigitsString.toInt() > MAX_PRECISION_DIGITS) MAX_PRECISION_DIGITS else precisionDigitsString.toInt()
        result.values.forEach {
            it.formattedQuantity = it.quantity.toBigDecimal().setScale(precisionDigits, RoundingMode.HALF_DOWN).toPlainString()
        }
    }

    private fun readCSV(fileName: String, dateArg: Date, result: TreeMap<String, Event>) {

        csvReader().open("/tmp/kotlinchallenge/$fileName") {
            readAllAsSequence().forEach { row: List<String> ->
                processEvent(row, dateArg, result)
            }
        }
    }

    private fun processEvent(row: List<String>, dateArg: Date, result: TreeMap<String, Event>) {

        val event = buildEvent(row)
        if (!event.date.after(dateArg)) {
            if (event.type.toUpperCase() == Constants.CANCEL) event.quantity = event.quantity * -1
            val hashKey = "${event.employeeId}-${event.awardId}"
            if (result[hashKey] == null) result[hashKey] = event
            else result[hashKey]!!.quantity += event.quantity
        }
    }

    private fun buildEvent(row: List<String>): Event {
        val type = row[Constants.CSV_HEADER_INDEX_TYPE]
        val employeeId = row[Constants.CSV_HEADER_INDEX_EMPLOYEE_ID]
        val employeeName = row[Constants.CSV_HEADER_INDEX_EMPLOYEE_NAME]
        val awardId = row[Constants.CSV_HEADER_INDEX_AWARD_ID]
        val date = SimpleDateFormat("yyyy-MM-dd").parse(row[Constants.CSV_HEADER_INDEX_DATE])
        val quantity = row[Constants.CSV_HEADER_INDEX_QUANTITY].replace(" ", "").toFloat()
        return Event(type, employeeId, employeeName, awardId, date, quantity)
    }
}
