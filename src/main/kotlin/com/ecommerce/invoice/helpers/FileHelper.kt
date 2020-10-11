package com.ecommerce.invoice.helpers

import com.ecommerce.invoice.exceptions.FileException
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
@Primary
class FileHelper : IFileHelper {

    override fun readFile(file: MultipartFile) =
            csvReader().readAllWithHeader(file.inputStream)

    override fun validateFile(file: MultipartFile) {
        if(file.isEmpty) {
            throw FileException("File must be present")
        }

        val fileExtension = file.originalFilename?.substringAfterLast('.', "")
        if (fileExtension != "csv") {
            throw FileException("File must be csv")
        }

        val fiftyMb = 50 * 1024 * 1024 //TODO use same value from application.yml
        if (file.size > fiftyMb) {
            throw FileException("File must be <50mb")
        }
    }
}