package com.ecommerce.invoice.dtos

import java.sql.Timestamp

class InvoiceDto(val invoiceNo: String, val invoiceDate: Timestamp, val customerId: Int?, val countryId: Int?) {

}
