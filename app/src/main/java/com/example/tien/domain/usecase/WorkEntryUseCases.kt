package com.example.tien.domain.usecase

import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.repository.WorkEntryRepository

class AddWorkEntryUseCase(private val repository: WorkEntryRepository) {
    suspend operator fun invoke(entry: WorkEntry) = repository.addWorkEntry(entry)
}

class EditWorkEntryUseCase(private val repository: WorkEntryRepository) {
    suspend operator fun invoke(entry: WorkEntry) = repository.editWorkEntry(entry)
}

class GetWorkEntriesUseCase(private val repository: WorkEntryRepository) {
    suspend operator fun invoke(): List<WorkEntry> = repository.getWorkEntries()
}

class DeleteWorkEntryUseCase(private val repository: WorkEntryRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteWorkEntry(id)
}

class ValidateWorkEntryUseCase {
    operator fun invoke(entry: WorkEntry): ValidationResult {
        if (entry.task.isBlank()) return ValidationResult(false, "Task cannot be empty")
        if (entry.salary < 0) return ValidationResult(false, "Salary must be positive")
        if (entry.breakMinutes < 0) return ValidationResult(false, "Break must be positive")
        if (entry.endTime.isBefore(entry.startTime)) return ValidationResult(false, "End time must be after start time")
        return ValidationResult(true, null)
    }
}

data class ValidationResult(val isValid: Boolean, val errorMessage: String?)
