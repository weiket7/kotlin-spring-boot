package com.ecommerce.invoice.helpers

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toTimestamp(): Timestamp {
    val pattern = if(this.contains("/")) {
        "M/d/yyyy H:mm"
    } else {
        "MM-dd-yy H:mm"
    }
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val localDateTime = LocalDateTime.parse(this, formatter)
    return Timestamp.valueOf(localDateTime)
}