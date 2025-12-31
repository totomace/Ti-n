package com.example.tien.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tien.domain.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val content: String,
    val createdAt: String, // ISO format yyyy-MM-dd'T'HH:mm:ss
    val updatedAt: String
)

fun NoteEntity.toDomain(): Note =
    Note(
        id = id,
        title = title,
        content = content,
        createdAt = java.time.LocalDateTime.parse(createdAt),
        updatedAt = java.time.LocalDateTime.parse(updatedAt)
    )

fun Note.toEntity(): NoteEntity =
    NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
