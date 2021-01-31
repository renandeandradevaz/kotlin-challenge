package com.kotlinchallenge.service

import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class EventServiceTest {

    @Before
    fun before() {
        val directoryName = "/tmp/kotlinchallenge/csvs/"
        val directory = File(directoryName)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    @Test
    fun processEventsTestExample1() {

        val csv = "VEST,E001,Alice Smith,ISO-001,2020-01-01,1000\n" +
                "VEST,E001,Alice Smith,ISO-001,2021-01-01,1000\n" +
                "VEST,E001,Alice Smith,ISO-002,2020-03-01,300\n" +
                "VEST,E001,Alice Smith,ISO-002,2020-04-01,500\n" +
                "VEST,E002,Bobby Jones,NSO-001,2020-01-02,100\n" +
                "VEST,E002,Bobby Jones,NSO-001,2020-02-02,200\n" +
                "VEST,E002,Bobby Jones,NSO-001,2020-03-02,300"

        val fileName = "/tmp/kotlinchallenge/csvs/example1.csv"
        var file = File(fileName)
        file.writeText(csv)

        val events = EventService().processEvents("csvs/example1.csv", "2020-04-01")

        assertEquals(3, events.size)

        assertEquals("E001", events[0].employeeId)
        assertEquals("Alice Smith", events[0].employeeName)
        assertEquals("ISO-001", events[0].awardId)
        assertEquals("1000", events[0].formattedQuantity)

        assertEquals("E002", events[2].employeeId)
        assertEquals("Bobby Jones", events[2].employeeName)
        assertEquals("NSO-001", events[2].awardId)
        assertEquals("600", events[2].formattedQuantity)
    }

    @Test
    fun processEventsTestExample2() {

        val csv = "VEST,E001,Alice Smith,ISO-001,2020-01-01,1000\n" +
                "CANCEL,E001,Alice Smith,ISO-001,2021-01-01,700"

        val fileName = "/tmp/kotlinchallenge/csvs/example2.csv"
        var file = File(fileName)
        file.writeText(csv)

        val events = EventService().processEvents("csvs/example2.csv", "2021-01-01")

        assertEquals(1, events.size)

        assertEquals("E001", events[0].employeeId)
        assertEquals("Alice Smith", events[0].employeeName)
        assertEquals("ISO-001", events[0].awardId)
        assertEquals("300", events[0].formattedQuantity)
    }

    @Test
    fun processEventsTestExample2ForcingNegativeVest() {

        val csv = "VEST,E001,Alice Smith,ISO-001,2020-01-01,1000\n" +
                "CANCEL,E001,Alice Smith,ISO-001,2021-01-01,700000"

        val fileName = "/tmp/kotlinchallenge/csvs/example2.csv"
        var file = File(fileName)
        file.writeText(csv)

        val events = EventService().processEvents("csvs/example2.csv", "2021-01-01")

        assertEquals(0, events.size)
    }

    @Test
    fun processEventsTestExample3PrecisionDigits() {

        val csv = "VEST,E001,Alice Smith,ISO-001,2020-01-01,1000.5\n" +
                "CANCEL,E001,Alice Smith,ISO-001,2021-01-01,700.75"

        val fileName = "/tmp/kotlinchallenge/csvs/example3.csv"
        var file = File(fileName)
        file.writeText(csv)

        val events = EventService().processEvents("csvs/example3.csv", "2021-01-01", "1")

        assertEquals(1, events.size)

        assertEquals("E001", events[0].employeeId)
        assertEquals("Alice Smith", events[0].employeeName)
        assertEquals("ISO-001", events[0].awardId)
        assertEquals("299.7", events[0].formattedQuantity)
    }
}
