package com.ecommerce.invoice

import com.ecommerce.invoice.exceptions.FileException
import com.ecommerce.invoice.helpers.FileHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import kotlin.test.assertEquals

@SpringBootTest
class FileHelperTests(@Autowired private val fileHelper: FileHelper) {

	@Test
	fun validateFile_IsEmpty_ThrowsException() {
		val csv = MockMultipartFile("data", null, null, null)
		val exception = assertThrows<FileException> {
			fileHelper.validateFile(csv)
		}
		assertEquals("File must be present", exception.message)
	}

	@Test
	fun validateFile_MoreThan50mb_ThrowsException() {
		val bytes = ByteArray(1024 * 1024 * 51)
		val csv = MockMultipartFile("data", "data.csv", "text/csv", bytes)
		val exception = assertThrows<FileException> {
			fileHelper.validateFile(csv)
		}
		assertEquals("File must be <50mb", exception.message)
	}

	@Test
	fun validateFile_NotCsv_ThrowsException() {
		val bytes = ByteArray(1024 * 1024 * 1)
		val csv = MockMultipartFile("data", "data.txt", "text/plain", bytes)
		val exception = assertThrows<FileException> {
			fileHelper.validateFile(csv)
		}
		assertEquals("File must be csv", exception.message)
	}

}
