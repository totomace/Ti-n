package com.example.tien.domain.usecase

import com.example.tien.domain.repository.WorkEntryRepository

class UpdatePaymentStatusUseCase(private val repository: WorkEntryRepository) {
    suspend operator fun invoke(id: Long, paidAmount: Long) {
        val entry = repository.getWorkEntries().find { it.id == id } ?: return
        val updated = entry.copy(
            paidAmount = paidAmount,
            isPaid = paidAmount >= entry.salary
        )
        repository.editWorkEntry(updated)
    }
}
