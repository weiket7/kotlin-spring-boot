package com.ecommerce.invoice.dtos

class InvoiceStockDto(val invoiceNo: String, val stockCode: String, val description: String?, val quantity: Int, val unitPrice: Double)