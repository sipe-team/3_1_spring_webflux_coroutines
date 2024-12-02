package com.sipe.week5.domain.common

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDateTime

@JsonIgnoreProperties(value = ["createdAt, modifiedAt"], allowGetters = true)
open class BaseEntity(
	/** 생성일 */
	@CreatedDate
	@Column("created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
	var createdAt: LocalDateTime = LocalDateTime.now(),

	/** 수정일 */
	@LastModifiedDate
	@Column("modified_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
	var modifiedAt: LocalDateTime = LocalDateTime.now(),
)
