package com.cns.demo.ordersevice.web

import com.cns.demo.ordersevice.domain.Order
import com.cns.demo.ordersevice.domain.OrderService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("orders")
class OrderController(private val orderService: OrderService) {
    @GetMapping
    fun getAllOrders(): Flux<Order> = orderService.getAllOrders()

    @GetMapping("{id}")
    fun getOrderById(@PathVariable id: Long): Mono<Order> = orderService.getOrder(id)

    @PostMapping
    fun submitOrder(@RequestBody @Valid orderRequest: OrderRequest): Mono<Order> =
        orderService.submitOrder(orderRequest.isbn, orderRequest.quantity)
}