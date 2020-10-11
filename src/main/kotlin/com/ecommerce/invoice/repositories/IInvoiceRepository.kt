package com.ecommerce.invoice.repositories

import com.ecommerce.invoice.entities.Invoice
import com.ecommerce.invoice.dtos.InvoiceDto
import com.ecommerce.invoice.dtos.InvoiceStockDto

interface IInvoiceRepository {
    fun search(invoiceNo: String): MutableList<Invoice>?
    fun batchInsertInvoiceStock(invoices: MutableList<InvoiceStockDto>)
    fun batchInsertInvoice(invoices: List<InvoiceDto>)
    fun listInvoice(page: Int, pageSize: Int): MutableList<Invoice>
    fun listInvoiceStock(invoiceNos: List<String>?): MutableList<InvoiceStockDto>
    fun getTotalNumberOfInvoice(): Int
}