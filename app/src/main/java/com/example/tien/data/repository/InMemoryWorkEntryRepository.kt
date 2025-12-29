package com.example.tien.data.repository

import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.repository.WorkEntryRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryWorkEntryRepository : WorkEntryRepository {
    private val entries = mutableListOf<WorkEntry>()
    private var nextId = 1L
    private val mutex = Mutex()

    override suspend fun addWorkEntry(entry: WorkEntry) {
        mutex.withLock {
            val newEntry = entry.copy(id = nextId++)
            entries.add(newEntry)
        }
    }

    override suspend fun editWorkEntry(entry: WorkEntry) {
        mutex.withLock {
            val index = entries.indexOfFirst { it.id == entry.id }
            if (index != -1) {
                entries[index] = entry
            }
        }
    }

    override suspend fun getWorkEntries(): List<WorkEntry> = mutex.withLock { entries.toList() }

    override suspend fun getWorkEntryById(id: Long): WorkEntry? = mutex.withLock { entries.find { it.id == id } }

    override suspend fun deleteWorkEntry(id: Long) {
        mutex.withLock {
            entries.removeAll { it.id == id }
        }
    }
}
