package com.ecommerce.invoice.models

data class ListInvoiceRequest(
        var page: Int = 1,
        var pageSize: Int = 24
)