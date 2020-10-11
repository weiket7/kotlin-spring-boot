package com.ecommerce.invoice.models

import com.ecommerce.invoice.entities.Invoice

class ListInvoiceResponse(val invoices: MutableList<Invoice>, val numberOfPages: Int)