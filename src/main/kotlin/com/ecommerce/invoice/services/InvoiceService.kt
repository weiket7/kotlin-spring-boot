package com.ecommerce.invoice.services

import com.ecommerce.invoice.helpers.IFileHelper
import com.ecommerce.invoice.helpers.toTimestamp
import com.ecommerce.invoice.entities.Invoice
import com.ecommerce.invoice.dtos.InvoiceDto
import com.ecommerce.invoice.dtos.InvoiceStockDto
import com.ecommerce.invoice.entities.InvoiceStock
import com.ecommerce.invoice.models.ListInvoiceResponse
import com.ecommerce.invoice.models.UploadProgress
import com.ecommerce.invoice.repositories.ICountryRepository
import com.ecommerce.invoice.repositories.IInvoiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.math.ceil

@Service
@Primary
class InvoiceService(
        @Autowired private val invoiceRepository: IInvoiceRepository,
        @Autowired private val countryRepository: ICountryRepository,
        @Autowired private val fileHelper: IFileHelper
) : IInvoiceService {

    companion object {
        private val Progress: MutableMap<Long, UploadProgress> = mutableMapOf()
    }

    override fun upload(file: MultipartFile, id: Long) {
        fileHelper.validateFile(file)

        Progress[id] = UploadProgress(10, "Step 1/4: Reading file")
        val rows: List<Map<String, String>> = fileHelper.readFile(file)
        Progress[id] = UploadProgress(10, "Step 2/4: File has been read")

        val countryMap = constructCountryMap(rows)
        countryRepository.batchInsert(countryMap)
        Progress[id] = UploadProgress(20, "Step 3/4: Countries have been inserted")

        val invoiceDtos = mutableListOf<InvoiceDto>()
        val invoiceStockDtos = mutableListOf<InvoiceStockDto>()
        val batchSize = 10000
        var batchCount = 0
        var count = 0
        val numberOfBatches = ceil(rows.size.toDouble() / batchSize).toInt()

        for (row in rows) {
            count++
            invoiceDtos.add(InvoiceDto(row.getValue("InvoiceNo"), row.getValue("InvoiceDate").toTimestamp(), row["CustomerID"]?.toIntOrNull(), countryMap[row.getValue("Country")]))
            invoiceStockDtos.add(InvoiceStockDto(row.getValue("InvoiceNo"), row.getValue("StockCode"), row["Description"], row.getValue("Quantity").toInt(), row.getValue("UnitPrice").toDouble()))

            if (invoiceStockDtos.size % batchSize == 0 || count == rows.size) {
                batchCount++
                val progress = ceil(batchCount.toDouble() / numberOfBatches * 0.8 * 100).toInt() + 20
                Progress[id] = UploadProgress(progress, "Step 4/4: Invoice stock batch $batchCount out of $numberOfBatches have been inserted")
                invoiceRepository.batchInsertInvoiceStock(invoiceStockDtos)
                invoiceStockDtos.clear()
            }
        }

        val invoiceDtosDistinct = constructInvoiceDtosDistinct(invoiceDtos)
        invoiceRepository.batchInsertInvoice(invoiceDtosDistinct)
        Progress[id] = UploadProgress(100, "Completed")
    }

    private fun constructInvoiceDtosDistinct(invoiceDtos: MutableList<InvoiceDto>): List<InvoiceDto> {
        return invoiceDtos.groupBy { it.invoiceNo }
                .entries.map { (invoiceNo, group) -> InvoiceDto(invoiceNo, group.first().invoiceDate, group.first().customerId, group.first().countryId) }
    }

    private fun constructCountryMap(rows: List<Map<String, String>>): MutableMap<String, Int> {
        val countryMap = mutableMapOf<String, Int>()
        var countryIndex = 0
        for (row in rows) {
            val countryName = row.getValue("Country")
            if (!countryMap.containsKey(countryName)) {
                countryIndex++
                countryMap[countryName] = countryIndex
            }
        }
        return countryMap
    }

    override fun getUploadProgress(id: Long): UploadProgress? {
        return Progress[id]
    }

    override fun search(invoiceNo: String): MutableList<Invoice>? {
        return invoiceRepository.search(invoiceNo)
    }

    override fun list(page: Int, pageSize: Int): ListInvoiceResponse {
        val invoices = invoiceRepository.listInvoice(page, pageSize)

        val invoiceNos = invoices.map { it.invoiceNo }
        val invoiceStockDtos = invoiceRepository.listInvoiceStock(invoiceNos)
        for(invoiceStockDto in invoiceStockDtos) {
            invoices.find{ it.invoiceNo == invoiceStockDto.invoiceNo }?.addInvoiceStock(InvoiceStock(invoiceStockDto.stockCode, invoiceStockDto.description, invoiceStockDto.quantity, invoiceStockDto.unitPrice))
        }
        val count = invoiceRepository.getTotalNumberOfInvoice()
        val numberOfPages = ceil(count.toDouble() / pageSize).toInt()
        return ListInvoiceResponse(invoices, numberOfPages)
    }
}