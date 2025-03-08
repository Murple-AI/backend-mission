package com.murple.murfy.infrastructure.user.jpa.entiy

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: ZonedDateTime = ZonedDateTime.now()
}
