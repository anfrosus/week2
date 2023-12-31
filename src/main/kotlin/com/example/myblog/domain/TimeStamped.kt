package com.example.myblog.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.OrderBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class TimeStamped {

    @CreatedDate
    @Column(updatable = false)
    var createdDate : LocalDateTime? = null

    @LastModifiedDate
    var modifiedDate: LocalDateTime? = null
}