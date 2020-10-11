package com.ecommerce.invoice

import com.ecommerce.invoice.dtos.InvoiceDto
import com.ecommerce.invoice.dtos.InvoiceStockDto
import com.ecommerce.invoice.entities.Invoice
import com.ecommerce.invoice.helpers.FileHelper
import com.ecommerce.invoice.repositories.ICountryRepository
import com.ecommerce.invoice.repositories.IInvoiceRepository
import com.ecommerce.invoice.services.InvoiceService
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.Month
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class InvoiceServiceTests {

    @Captor
    lateinit var captor: ArgumentCaptor<MutableList<InvoiceDto>>

    private val expectedInvoiceDto1 = InvoiceDto("536365", Timestamp.valueOf(LocalDateTime.of(2010, Month.DECEMBER, 1, 8, 26)), 17850, 1)
    private val expectedInvoiceDto2 = InvoiceDto("536366", Timestamp.valueOf(LocalDateTime.of(2010, Month.DECEMBER, 1, 8, 26)), 17851, 1)

    @Test
    fun upload_Success() {
        val invoiceRepository = mock(IInvoiceRepository::class.java)
        val countryRepository = mock(ICountryRepository::class.java)
        val invoiceService = InvoiceService(invoiceRepository, countryRepository, FileHelper())

        val csvString: String = "InvoiceNo,StockCode,Description,Quantity,InvoiceDate,UnitPrice,CustomerID,Country\n" +
                "536365,85123A,WHITE HANGING HEART T-LIGHT HOLDER,6,12/1/2010 8:26,2.55,17850,United Kingdom\n" +
                "536366,84406B,CREAM CUPID HEARTS COAT HANGER,8,12-01-10 8:26,2.75,17851,United Kingdom"
        val csv = MockMultipartFile("data", "data.csv", "text/plain", csvString.toByteArray())

        invoiceService.upload(csv, 123)

        verify(invoiceRepository).batchInsertInvoice(MockitoHelper.capture(captor))
        val invoiceDto1 = captor.value.find { x -> x.invoiceNo == "536365" }
        assertTrue(ReflectionEquals(expectedInvoiceDto1).matches(invoiceDto1))

        val invoice2 = captor.value.find { x -> x.invoiceNo == "536366" }
        assertTrue(ReflectionEquals(expectedInvoiceDto2).matches(invoice2))
    }

    @Test
    fun list_Success() {
        val invoiceRepository = mock(IInvoiceRepository::class.java)
        val invoiceNo = "536365"
        setUpListInvoice(invoiceRepository, invoiceNo)
        setUpListInvoiceStock(invoiceRepository, invoiceNo)

        val countryRepository = mock(ICountryRepository::class.java)
        val invoiceService = InvoiceService(invoiceRepository, countryRepository, FileHelper())

        val resultInvoices = invoiceService.list(1, 24).invoices

        assertEquals(1, resultInvoices.size)
        val resultInvoice = resultInvoices.find { it.invoiceNo == invoiceNo }
        assertEquals(1, resultInvoice?.invoiceStocks?.size)
    }

    private fun setUpListInvoice(invoiceRepository: IInvoiceRepository, invoiceNo: String) {
        val timestamp = Timestamp.valueOf(LocalDateTime.of(2010, Month.DECEMBER, 20, 11, 21))
        val invoice = Invoice(invoiceNo, timestamp, 17850, "United Kingdom")
        val invoices = mutableListOf(invoice)
        `when`(invoiceRepository.listInvoice(anyInt(), anyInt())).thenReturn(invoices)
    }

    private fun setUpListInvoiceStock(invoiceRepository: IInvoiceRepository, invoiceNo: String) {
        val invoiceStockDto = InvoiceStockDto(invoiceNo, "85123A", "WHITE HANGING HEART T-LIGHT HOLDER", 6, 2.55)
        val invoiceStockDtos = mutableListOf(invoiceStockDto)
        `when`(invoiceRepository.listInvoiceStock(anyList())).thenReturn(invoiceStockDtos)
    }


}