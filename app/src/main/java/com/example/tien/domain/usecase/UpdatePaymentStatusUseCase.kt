package com.example.tien.domain.usecase

import com.example.tien.domain.repository.WorkEntryRepository

class UpdatePaymentStatusUseCase(private val repository: WorkEntryRepository) {
    suspend operator fun invoke(id: Long, isPaid: Boolean) {
        val entry = repository.getWorkEntries().find { it.id == id } ?: return
        val updated = entry.copy(isPaid = isPaid)
        repository.editWorkEntry(updated)
    }
}
