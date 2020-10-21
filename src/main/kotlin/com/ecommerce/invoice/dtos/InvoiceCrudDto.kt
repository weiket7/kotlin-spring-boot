package com.ecommerce.invoice.dtos

import java.sql.Timestamp
import javax.persistence.*
import java.io.Serializable

@Entity
@Table(name = "Invoice")
class InvoiceCrudDto(
        @Id
        var id: Int? = null,

        @Column(name = "invoiceno")
        var invoiceNo: String? = null,

        @Column(name = "invoicedate")
        var invoiceDate: Timestamp? = null,

        @Column(name = "customerid")
        var customerId: Int? = null,

        @OneToOne
        @JoinColumn(name = "countryid", referencedColumnName = "id")
        var country: CountryCrudDto? = null,

//        @Column(name = "countryid")
//        var countryId: Int? = null

        @OneToMany
        @JoinColumn(name = "invoiceno" ,referencedColumnName = "invoiceno")
        var stock: MutableList<InvoiceStockCrudDto>? = mutableListOf()

) : Serializable