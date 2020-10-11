package com.ecommerce.invoice.repositories

interface ICountryRepository {
    fun batchInsert(countries: MutableMap<String, Int>)
}
