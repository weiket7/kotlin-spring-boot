package com.ecommerce.invoice.controllers

import com.ecommerce.invoice.dtos.InvoiceCrudDto
import com.ecommerce.invoice.repositories.InvoiceCrudRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("api/invoicecrud")
@RestController
class InvoiceCrudController(@Autowired val invoiceCrudRepository: InvoiceCrudRepository) {

    @GetMapping("/{invoiceNo}")
    fun getInvoice(@PathVariable("invoiceNo") invoiceNo: String): InvoiceCrudDto? {
        return invoiceCrudRepository.findByInvoiceNo(invoiceNo)
    }

}
