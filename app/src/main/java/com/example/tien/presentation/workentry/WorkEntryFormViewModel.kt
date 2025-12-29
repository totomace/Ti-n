package com.example.tien.presentation.workentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.usecase.AddWorkEntryUseCase
import com.example.tien.domain.usecase.EditWorkEntryUseCase
import com.example.tien.domain.usecase.ValidateWorkEntryUseCase
import com.example.tien.domain.usecase.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class WorkEntryFormViewModel(
    private val addWorkEntry: AddWorkEntryUseCase,
    private val editWorkEntry: EditWorkEntryUseCase,
    private val validateWorkEntry: ValidateWorkEntryUseCase
) : ViewModel() {
    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState

    fun onDateChange(date: LocalDate) {
        _formState.value = _formState.value.copy(date = date)
    }

    fun onStartTimeChange(time: LocalTime) {
        _formState.value = _formState.value.copy(startTime = time)
    }

    fun onEndTimeChange(time: LocalTime) {
        _formState.value = _formState.value.copy(endTime = time)
    }

    fun onBreakMinutesChange(minutes: String) {
        val value = minutes.toIntOrNull() ?: 0
        _formState.value = _formState.value.copy(breakMinutes = value)
    }

    fun onTaskChange(task: String) {
        _formState.value = _formState.value.copy(task = task)
    }

    fun onSalaryChange(salary: String) {
        val value = salary.toLongOrNull() ?: 0L
        _formState.value = _formState.value.copy(salary = value)
    }

    fun submit(isEdit: Boolean = false, onSuccess: () -> Unit = {}) {
        val entry = WorkEntry(
            id = _formState.value.id,
            date = _formState.value.date,
            startTime = _formState.value.startTime,
            endTime = _formState.value.endTime,
            breakMinutes = _formState.value.breakMinutes,
            task = _formState.value.task,
            salary = _formState.value.salary
        )
        val validation: ValidationResult = validateWorkEntry(entry)
        if (!validation.isValid) {
            _formState.value = _formState.value.copy(error = validation.errorMessage)
            return
        }
        viewModelScope.launch {
            if (isEdit) {
                editWorkEntry(entry)
            } else {
                addWorkEntry(entry)
            }
            _formState.value = FormState() // Reset form
            onSuccess()
        }
    }

    data class FormState(
        val id: Long = 0L,
        val date: LocalDate = LocalDate.now(),
        val startTime: LocalTime = LocalTime.of(8, 0),
        val endTime: LocalTime = LocalTime.of(17, 0),
        val breakMinutes: Int = 0,
        val task: String = "",
        val salary: Long = 0L,
        val error: String? = null
    )
}
