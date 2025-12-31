package com.example.tien.domain.model

import java.time.LocalDateTime

data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
