package com.example.tien.domain.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * Entity representing a manual work entry for the timesheet app.
 */
data class WorkEntry(
    val id: Long = 0L,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val breakMinutes: Int,
    val task: String,
    val salary: Long,
    val isPaid: Boolean = false,
    val paidAmount: Long = 0L,
    val notes: String = ""
)