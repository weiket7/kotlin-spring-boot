package com.ecommerce.invoice

import com.fasterxml.jackson.databind.ObjectMapper
import com.ecommerce.invoice.entities.Invoice
import com.ecommerce.invoice.models.ListInvoiceResponse
import com.ecommerce.invoice.models.SearchInvoiceRequest
import com.ecommerce.invoice.services.IInvoiceService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.Month
import kotlin.test.assertEquals
import com.fasterxml.jackson.module.kotlin.*
import org.mockito.BDDMockito.given
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTests {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var invoiceService : IInvoiceService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private var jsonMapper = jacksonObjectMapper()

    @Test
    fun search_Success() {
        val invoiceNo = "536365"
        val timestamp = Timestamp.valueOf(LocalDateTime.of(2010, Month.DECEMBER, 20, 11, 21))
        val invoices = mutableListOf(Invoice(invoiceNo, timestamp, 17850, "United Kingdom"))
        given(invoiceService.search(invoiceNo)).willReturn(invoices)

        val requestJson = objectMapper.writeValueAsString(SearchInvoiceRequest(invoiceNo))
        val request = post("/api/invoice/search").contentType(MediaType.APPLICATION_JSON).content(requestJson)

        val result = mvc.perform(request).andReturn()

        val resultInvoices = jsonMapper.readValue<MutableList<Invoice>>(result.response.contentAsString)
        val resultInvoice = resultInvoices.find { it.invoiceNo == invoiceNo }
        assertEquals(invoiceNo, resultInvoice?.invoiceNo)
    }

    @Test
    fun search_EmptyRequest_ReturnBadRequest() {
        val invoices = mutableListOf<Invoice>()
        given(invoiceService.search("123")).willReturn(invoices)

        val emptyRequest ="{}"
        val request = post("/api/invoice/search").contentType(MediaType.APPLICATION_JSON).content(emptyRequest)

        val result = mvc.perform(request).andReturn()
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.response.status)
    }

    @Test
    fun list_Success() {
        val invoiceNo = "536365"
        val timestamp = Timestamp.valueOf(LocalDateTime.of(2010, Month.DECEMBER, 20, 11, 21))
        val invoices = mutableListOf(Invoice(invoiceNo, timestamp, 17850, "United Kingdom"))
        given(invoiceService.list(1, 24)).willReturn(ListInvoiceResponse(invoices, 1))

        val request = get("/api/invoice/list?page=1&pageSize=24").contentType(MediaType.APPLICATION_JSON)

        val result = mvc.perform(request).andReturn()

        val response = jsonMapper.readValue<ListInvoiceResponse>(result.response.contentAsString)
        assertEquals(1, response.invoices.size)
        val resultInvoice = response.invoices.find { it.invoiceNo == invoiceNo }
        assertEquals(invoiceNo, resultInvoice?.invoiceNo)
    }

}