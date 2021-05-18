package com.cns.demo.ordersevice.book

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.io.IOException


class BookClientTests() {
    lateinit var mockWebServer: MockWebServer
    lateinit var bookClient: BookClient

    @BeforeEach
    @Throws(IOException::class)
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val bookClientProperties = BookClientProperties()
        bookClientProperties.catalogServiceUrl = mockWebServer.url("/").uri()
        this.bookClient = BookClient(bookClientProperties, WebClient.builder())
    }

    @AfterEach
    @Throws(IOException::class)
    fun clean() {
        mockWebServer.shutdown()
    }

    @Test
    fun whenBookExistsThenReturnBook() {
        val bookIsbn = "1234567890"
        val mockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody("{\"isbn\":\"$bookIsbn\",\"title\":\"Book Title\", \"author\":\"Book Author\", \"price\":\"9.90\"}")
        mockWebServer.enqueue(mockResponse)

        val book = bookClient.getBookByIsbn(bookIsbn)
        StepVerifier.create(book)
            .assertNext { it.isbn == bookIsbn }
            //.expectNextMatches {  it.isbn == bookIsbn }
            .verifyComplete()
    }


}