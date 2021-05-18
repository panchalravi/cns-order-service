package com.cns.demo.ordersevice.persistence

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
//@EntityListeners(AuditingEntityListener::class) - Not required, done automatically
open class PersistableEntity(
    @Id var id: Long = 0,
    @CreatedDate var createdDate: Long = 0,
    @LastModifiedDate var lastModifiedDate: Long = 0,
    @Version var version: Int = 0
)
