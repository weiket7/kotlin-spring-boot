package com.ecommerce.invoice.dtos

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "country")
class CountryCrudDto(
        @Id var id: Int? = null,
        @Column(name = "name") var name: String? = null
)