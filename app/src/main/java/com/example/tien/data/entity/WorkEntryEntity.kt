package com.example.tien.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "work_entries")
data class WorkEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: String, // ISO format yyyy-MM-dd
    val startTime: String, // HH:mm
    val endTime: String, // HH:mm
    val breakMinutes: Int,
    val task: String,
    val salary: Long,
    val isPaid: Boolean = false,
    val paidAmount: Long = 0L,
    val notes: String = ""
)

// Mapping functions
fun WorkEntryEntity.toDomain(): com.example.tien.domain.model.WorkEntry =
    com.example.tien.domain.model.WorkEntry(
        id = id,
        date = LocalDate.parse(date),
        startTime = LocalTime.parse(startTime),
        endTime = LocalTime.parse(endTime),
        breakMinutes = breakMinutes,
        task = task,
        salary = salary,
        isPaid = isPaid,
        paidAmount = paidAmount,
        notes = notes
    )

fun com.example.tien.domain.model.WorkEntry.toEntity(): WorkEntryEntity =
    WorkEntryEntity(
        id = id,
        date = date.toString(),
        startTime = startTime.toString(),
        endTime = endTime.toString(),
        breakMinutes = breakMinutes,
        task = task,
        salary = salary,
        isPaid = isPaid,
        paidAmount = paidAmount,
        notes = notes
    )
