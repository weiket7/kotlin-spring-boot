package com.ecommerce.invoice.entities

class InvoiceStock(
        val stockCode: String,
        val description: String?,
        val quantity: Int,
        val unitPrice: Double
) {
}