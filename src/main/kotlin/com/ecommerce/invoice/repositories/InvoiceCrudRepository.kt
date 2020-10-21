package com.ecommerce.invoice.repositories

import com.ecommerce.invoice.dtos.InvoiceCrudDto
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceCrudRepository: CrudRepository<InvoiceCrudDto, Int>{
    fun findByInvoiceNo(invoiceNo: String): InvoiceCrudDto?
}
