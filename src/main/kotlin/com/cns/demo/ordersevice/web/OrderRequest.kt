package com.cns.demo.ordersevice.web

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class OrderRequest(
    @field: NotBlank
    val isbn: String,

    @field: NotNull
    @field: Min(1)
    @field: Max(5)
    val quantity: Int
)