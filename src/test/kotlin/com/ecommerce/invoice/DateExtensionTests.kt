package com.ecommerce.invoice

import com.ecommerce.invoice.helpers.toTimestamp
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.Month
import kotlin.test.assertEquals

@SpringBootTest
class DateExtensionTests {

    @Test
    fun toTimestamp_MMDDYY_UsingDash_1() {
        val input = "12-01-10 08:26"
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2010, Month.DECEMBER, 1, 8, 26)), input.toTimestamp())
    }

    @Test
    fun toTimestamp2_MMDDYY_UsingDash_2() {
        val input = "12-20-10 11:21"
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2010, Month.DECEMBER, 20, 11, 21)), input.toTimestamp())
    }

    @Test
    fun toTimestamp3_MMDDYYYY_UsingSlash_1() {
        val input = "5/13/2011 10:44"
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2011, Month.MAY, 13, 10, 44)), input.toTimestamp())
    }

    @Test
    fun toTimestamp3_MMDDYYYY_UsingSlash_2() {
        val input = "11/13/2011 10:44"
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2011, Month.NOVEMBER, 13, 10, 44)), input.toTimestamp())
    }

}