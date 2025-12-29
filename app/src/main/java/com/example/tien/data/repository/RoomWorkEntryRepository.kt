package com.example.tien.data.repository

import com.example.tien.data.dao.WorkEntryDao
import com.example.tien.data.entity.toDomain
import com.example.tien.data.entity.toEntity
import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.repository.WorkEntryRepository

class RoomWorkEntryRepository(private val dao: WorkEntryDao) : WorkEntryRepository {
    override suspend fun addWorkEntry(entry: WorkEntry) {
        dao.insert(entry.toEntity())
    }

    override suspend fun editWorkEntry(entry: WorkEntry) {
        dao.update(entry.toEntity())
    }

    override suspend fun getWorkEntries(): List<WorkEntry> =
        dao.getAll().map { it.toDomain() }

    override suspend fun getWorkEntryById(id: Long): WorkEntry? =
        dao.getById(id)?.toDomain()

    override suspend fun deleteWorkEntry(id: Long) {
        dao.getById(id)?.let { dao.delete(it) }
    }
}
