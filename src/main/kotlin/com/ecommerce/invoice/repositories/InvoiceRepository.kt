package com.ecommerce.invoice.repositories

import com.ecommerce.invoice.dtos.InvoiceDto
import com.ecommerce.invoice.dtos.InvoiceStockDto
import com.ecommerce.invoice.entities.Invoice
import com.ecommerce.invoice.entities.InvoiceStock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
@Primary
class InvoiceRepository(
        @Autowired private val jdbcTemplate: JdbcTemplate,
        @Autowired private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : IInvoiceRepository {

    override fun batchInsertInvoice(invoices: List<InvoiceDto>) {
        val sql = "insert into Invoice (InvoiceNo, InvoiceDate, CustomerId, CountryId)\n" +
                "values (?, ?, ?, ?)"

        jdbcTemplate.batchUpdate(sql, invoices, invoices.size) { ps, invoice ->
            ps.setString(1, invoice.invoiceNo)
            ps.setTimestamp(2, invoice.invoiceDate)
            if(invoice.customerId == null) {
                ps.setInt(3, 0)
            } else {
                ps.setInt(3, invoice.customerId)
            }
            ps.setInt(4, invoice.countryId!!)
        }
    }

    override fun batchInsertInvoiceStock(invoices: MutableList<InvoiceStockDto>) {
        val sql = "insert into InvoiceStock (InvoiceNo, StockCode, Description, Quantity, UnitPrice)\n" +
                "values (?, ?, ?, ?, ?)"

        jdbcTemplate.batchUpdate(sql, invoices, invoices.size) { ps, invoice ->
            ps.setString(1, invoice.invoiceNo)
            ps.setString(2, invoice.stockCode)
            ps.setString(3, invoice.description)
            ps.setInt(4, invoice.quantity)
            ps.setDouble(5, invoice.unitPrice)
        }
    }

    override fun search(invoiceNo: String): MutableList<Invoice>? {
        val sql = "select InvoiceStock.InvoiceNo, StockCode, Description, Quantity, InvoiceDate, UnitPrice, CustomerId, Country.Name as CountryName" +
                " from Invoice " +
                " join Country on Invoice.CountryId = Country.Id" +
                " join InvoiceStock on Invoice.InvoiceNo = InvoiceStock.InvoiceNo" +
                " where Invoice.InvoiceNo = ?"

        return jdbcTemplate.query(sql, invoiceWithStockMapper, invoiceNo)
    }

    private val invoiceWithStockMapper: ResultSetExtractor<MutableList<Invoice>> = ResultSetExtractor { resultSet: ResultSet ->
        val invoices: MutableList<Invoice> = mutableListOf()
        var invoiceNo: String? = null
        var currentInvoice: Invoice? = null
        while (resultSet.next()) {
            if (currentInvoice == null || invoiceNo != resultSet.getString("InvoiceNo")) {
                invoiceNo = resultSet.getString("InvoiceNo")
                currentInvoice = Invoice(resultSet.getString("InvoiceNo"), resultSet.getTimestamp("InvoiceDate"), resultSet.getInt("CustomerId"), resultSet.getString("CountryName"))
                invoices.add(currentInvoice)
            }
            currentInvoice.addInvoiceStock(InvoiceStock(resultSet.getString("StockCode"), resultSet.getString("Description"), resultSet.getInt("Quantity"), resultSet.getDouble("UnitPrice")))
        }
        invoices
    }

    override fun listInvoice(page: Int, pageSize: Int): MutableList<Invoice> {
        val offset = (page-1) * pageSize

        val sql = "select InvoiceNo, InvoiceDate, CustomerId, Country.Name as CountryName" +
                " from Invoice " +
                " join Country on Invoice.CountryId = Country.Id" +
                " limit ? offset ?"
        return jdbcTemplate.query(sql, invoiceMapper, pageSize, offset)
    }

    override fun getTotalNumberOfInvoice(): Int {
        val sql = "select count(1) from invoice"
        val count = jdbcTemplate.queryForObject(sql, Int::class.java)
        return count ?: 0
    }

    private val invoiceMapper: RowMapper<Invoice> = RowMapper { resultSet: ResultSet, _: Int ->
        Invoice(resultSet.getString("InvoiceNo"), resultSet.getTimestamp("InvoiceDate"), resultSet.getInt("CustomerId"), resultSet.getString("CountryName"))
    }

    override fun listInvoiceStock(invoiceNos: List<String>?): MutableList<InvoiceStockDto> {
        val parameters = MapSqlParameterSource()
        parameters.addValue("invoiceNos", invoiceNos)

        val sql = "select InvoiceNo, StockCode, Description, Quantity, UnitPrice" +
                " from InvoiceStock " +
                " where InvoiceNo in (:invoiceNos)"
        return namedParameterJdbcTemplate.query(sql, parameters, invoiceStockDtoMapper)
    }

    private val invoiceStockDtoMapper: RowMapper<InvoiceStockDto> = RowMapper { resultSet: ResultSet, _: Int ->
        InvoiceStockDto(resultSet.getString("InvoiceNo"), resultSet.getString("StockCode"), resultSet.getString("Description"), resultSet.getInt("Quantity"), resultSet.getDouble("UnitPrice"))
    }

}