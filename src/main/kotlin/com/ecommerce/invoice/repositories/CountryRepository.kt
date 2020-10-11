package com.ecommerce.invoice.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
@Primary
class CountryRepository(@Autowired val jdbcTemplate: JdbcTemplate) : ICountryRepository {

    override fun batchInsert(countries: MutableMap<String, Int>) {
        val sql = "insert into Country (Id, Name)\n" +
                "values (?, ?)"

        jdbcTemplate.batchUpdate(sql, countries.entries, countries.size) { ps, t ->
            ps.setInt(1, t.value)
            ps.setString(2, t.key)
        }
    }

}