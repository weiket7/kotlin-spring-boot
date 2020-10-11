package com.ecommerce.invoice.models

import com.fasterxml.jackson.annotation.JsonProperty

 class SearchInvoiceRequest(@JsonProperty val invoiceNo: String)