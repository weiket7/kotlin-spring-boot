package com.ecommerce.invoice.services

import com.ecommerce.invoice.entities.Invoice
import com.ecommerce.invoice.models.ListInvoiceResponse
import com.ecommerce.invoice.models.UploadProgress
import org.springframework.web.multipart.MultipartFile

interface IInvoiceService {
    fun search(invoiceNo: String): MutableList<Invoice>?
    fun list(page: Int, pageSize: Int): ListInvoiceResponse
    fun getUploadProgress(id: Long): UploadProgress?
    fun upload(file: MultipartFile, id: Long)
}