package com.sipe.week5.domain.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.ZoneId

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity {
	@CreatedDate
	@Column(updatable = false, name = "created_at")
	var createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

	@LastModifiedDate
	@Column(name = "updated_at")
	var updatedAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
}
