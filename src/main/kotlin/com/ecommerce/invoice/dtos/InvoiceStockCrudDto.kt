package com.ecommerce.invoice.dtos

import javax.persistence.*

@Entity
@Table(name = "invoicestock")
class InvoiceStockCrudDto(
        //@ManyToOne
        //@JoinColumn(name = "invoiceno" ,referencedColumnName = "invoiceno")
        //var invoiceCrudDto: InvoiceCrudDto? = null,

        @Id @Column(name="id") var id: Int? = null,
        @Column(name="invoiceno") var invoiceNo: String? = null,
        @Column(name="stockcode") var stockCode: String? = null,
        @Column(name="description") var description: String? = null,
        @Column(name="quantity") var quantity: Int? = null,
        @Column(name="unitprice") var unitPrice: Double? = null
)