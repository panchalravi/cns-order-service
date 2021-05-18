package com.cns.demo.ordersevice.book

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI
import javax.validation.constraints.NotNull

@ConfigurationProperties(prefix = "polar")
class BookClientProperties {
    /**
     * The URL of the CatalogService application.
     */
    @NotNull
    lateinit var catalogServiceUrl: URI
}