package com.ecommerce.invoice

import org.mockito.ArgumentCaptor

//http://derekwilson.net/blog/2018/10/19/mockito-kotlin-2
object MockitoHelper {
    // use this in place of captor.capture() if you are trying to capture an argument that is not nullable
    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}