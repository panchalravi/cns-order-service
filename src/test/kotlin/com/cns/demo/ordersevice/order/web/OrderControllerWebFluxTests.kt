package com.cns.demo.ordersevice.order.web

import com.cns.demo.ordersevice.domain.Order
import com.cns.demo.ordersevice.domain.OrderService
import com.cns.demo.ordersevice.domain.OrderStatus
import com.cns.demo.ordersevice.web.OrderController
import com.cns.demo.ordersevice.web.OrderRequest
import org.assertj.core.api.BDDAssertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@WebFluxTest(OrderController::class)
class OrderControllerWebFluxTests {
    @Autowired
    lateinit var webClient: WebTestClient

    @MockBean
    lateinit var orderService: OrderService

    @Test
    fun whenBookNotAvailableThenRejectOrder() {
        val orderRequest = OrderRequest("1234567890", 3)
        val expectedOrder = Order(orderRequest.isbn, quantity = orderRequest.quantity, status = OrderStatus.REJECTED)

        BDDMockito.given(orderService.submitOrder(orderRequest.isbn, orderRequest.quantity))
            .willReturn(Mono.just(expectedOrder))

        val createdOrder = webClient.post().uri("/orders")
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody<Order>().returnResult().responseBody


        BDDAssertions.then(createdOrder).isNotNull
        BDDAssertions.then(createdOrder?.status).isEqualTo(OrderStatus.REJECTED)
    }

}