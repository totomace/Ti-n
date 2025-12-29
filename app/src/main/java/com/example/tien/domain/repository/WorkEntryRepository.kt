package com.example.tien.domain.repository

import com.example.tien.domain.model.WorkEntry

interface WorkEntryRepository {
    suspend fun addWorkEntry(entry: WorkEntry)
    suspend fun editWorkEntry(entry: WorkEntry)
    suspend fun getWorkEntries(): List<WorkEntry>
    suspend fun getWorkEntryById(id: Long): WorkEntry?
    suspend fun deleteWorkEntry(id: Long)
}
