package com.cns.demo.ordersevice.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderRepository: ReactiveCrudRepository<Order, Long>