package com.ecommerce.invoice.models

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.servlet.multipart")
class FileConfiguration {
    @Value("max-file-size")
    lateinit var maxFileSize: String
    @Value("max-request-size")
    lateinit var maxRequestSize: String
}