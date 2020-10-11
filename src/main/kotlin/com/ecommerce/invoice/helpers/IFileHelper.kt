package com.ecommerce.invoice.helpers

import org.springframework.web.multipart.MultipartFile

interface IFileHelper {
    fun readFile(file: MultipartFile): List<Map<String, String>>
    fun validateFile(file: MultipartFile)
}