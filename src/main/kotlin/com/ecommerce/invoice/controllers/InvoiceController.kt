package com.ecommerce.invoice.controllers

import com.ecommerce.invoice.entities.Invoice
import com.ecommerce.invoice.models.ListInvoiceRequest
import com.ecommerce.invoice.models.ListInvoiceResponse
import com.ecommerce.invoice.models.SearchInvoiceRequest
import com.ecommerce.invoice.models.UploadProgress
import com.ecommerce.invoice.services.IInvoiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RequestMapping("api/invoice")
@RestController
class InvoiceController(@Autowired val invoiceService: IInvoiceService) {

    @GetMapping("list")
    fun list(request: ListInvoiceRequest): ListInvoiceResponse {
       return invoiceService.list(request.page, request.pageSize)
    }

    @PostMapping("search")
    fun search(@RequestBody request: SearchInvoiceRequest): MutableList<Invoice>? {
        return invoiceService.search(request.invoiceNo)
    }

    @GetMapping("uploadProgress")
    @CrossOrigin
    fun uploadProgress(@RequestParam id: Long): UploadProgress? {
        return invoiceService.getUploadProgress(id)
    }

    @PostMapping("upload")
    fun upload(@RequestParam file: MultipartFile, @RequestParam id: Long) {
        invoiceService.upload(file, id)
    }
}