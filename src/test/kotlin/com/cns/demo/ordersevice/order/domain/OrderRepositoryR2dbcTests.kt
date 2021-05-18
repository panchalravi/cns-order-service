package com.cns.demo.ordersevice.order.domain

import com.cns.demo.ordersevice.domain.Order
import com.cns.demo.ordersevice.domain.OrderRepository
import com.cns.demo.ordersevice.domain.OrderStatus
import com.cns.demo.ordersevice.persistence.DataConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import reactor.test.StepVerifier


@DataR2dbcTest
@Testcontainers
@Import(DataConfig::class)
class OrderRepositoryR2dbcTests {

    @Autowired
    lateinit var orderRepository: OrderRepository

    companion object {
        private val postgresql = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13"))

        @DynamicPropertySource
        fun postgresqlProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTests::r2dbcUrl)
            registry.add("spring.r2dbc.username") { postgresql.username }
            registry.add("spring.r2dbc.password") { postgresql.password }
            registry.add("spring.flyway.url") { postgresql.jdbcUrl }
            registry.add("spring.flyway.user") { postgresql.username }
            registry.add("spring.flyway.password") { postgresql.password }
        }

        private fun r2dbcUrl(): String {
            return String.format(
                "r2dbc:postgresql://%s:%s/%s",
                postgresql.containerIpAddress,
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgresql.databaseName
            );
        }
    }

    @Test
    fun createRejectedOrder() {
        val rejectedOrder = Order("1234567890", quantity = 3, status = OrderStatus.REJECTED)
        StepVerifier.create(orderRepository.save(rejectedOrder))
            .expectNextMatches {
                it.status == OrderStatus.REJECTED
            }
            .verifyComplete()
    }
}