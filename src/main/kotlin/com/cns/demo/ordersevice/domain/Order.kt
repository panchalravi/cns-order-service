package com.cns.demo.ordersevice.domain

import com.cns.demo.ordersevice.persistence.PersistableEntity
import org.springframework.data.relational.core.mapping.Table

@Table("orders")
data class Order(
    var bookIsbn: String,
    var bookName: String = "",
    var bookPrice: Double = 0.0,
    var quantity: Int,
    var status: OrderStatus
): PersistableEntity()

enum class OrderStatus {
    ACCEPTED,
    REJECTED,
    DISPATCHED
}
