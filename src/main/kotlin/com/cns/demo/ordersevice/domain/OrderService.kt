package com.cns.demo.ordersevice.domain

import com.cns.demo.ordersevice.book.Book
import com.cns.demo.ordersevice.book.BookClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderService(
    private val bookClient: BookClient,
    private val orderRepository: OrderRepository
) {

    fun getAllOrders(): Flux<Order> = orderRepository.findAll()

    fun getOrder(id: Long): Mono<Order> = orderRepository.findById(id)

    fun submitOrder(isbn: String, quantity: Int): Mono<Order> {
        return bookClient.getBookByIsbn(isbn)
            .flatMap { Mono.just(buildAcceptedOrder(it, quantity)) }
            .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
            .flatMap { orderRepository.save(it) }
    }

    private fun buildAcceptedOrder(book: Book, quantity: Int): Order =
        Order(book.isbn, book.title, book.price, quantity, OrderStatus.ACCEPTED)

    private fun buildRejectedOrder(isbn: String, quantity: Int): Order =
        Order(isbn, quantity = quantity, status= OrderStatus.REJECTED)
}