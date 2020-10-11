package com.ecommerce.invoice.entities

import java.sql.Timestamp

class Invoice(
        var invoiceNo: String,
        var invoiceDate: Timestamp,
        var customerId: Int?,
        var countryName: String
) {
    fun addInvoiceStock(invoiceStock: InvoiceStock) {
        invoiceStocks.add(invoiceStock)
    }

    var invoiceStocks: MutableList<InvoiceStock> = mutableListOf()
}