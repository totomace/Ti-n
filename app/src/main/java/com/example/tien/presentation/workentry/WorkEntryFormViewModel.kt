package com.example.tien.presentation.workentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.usecase.AddWorkEntryUseCase
import com.example.tien.domain.usecase.EditWorkEntryUseCase
import com.example.tien.domain.usecase.ValidateWorkEntryUseCase
import com.example.tien.domain.usecase.ValidationResult
import com.example.tien.util.CurrencyInputFormatter
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
        val value = CurrencyInputFormatter.parseToLong(salary) ?: 0L
        _formState.value = _formState.value.copy(salary = value, salaryInput = salary)
    }

    fun onPaidAmountChange(amount: String) {
        val value = CurrencyInputFormatter.parseToLong(amount) ?: 0L
        _formState.value = _formState.value.copy(paidAmount = value, paidAmountInput = amount)
    }

    fun onNotesChange(notes: String) {
        _formState.value = _formState.value.copy(notes = notes)
    }

    fun submit(onSuccess: () -> Unit = {}) {
        val entry = WorkEntry(
            id = _formState.value.id,
            date = _formState.value.date,
            startTime = _formState.value.startTime,
            endTime = _formState.value.endTime,
            breakMinutes = _formState.value.breakMinutes,
            task = _formState.value.task,
            salary = _formState.value.salary,
            isPaid = _formState.value.paidAmount >= _formState.value.salary,
            paidAmount = _formState.value.paidAmount,
            notes = _formState.value.notes
        )
        val validation: ValidationResult = validateWorkEntry(entry)
        if (!validation.isValid) {
            _formState.value = _formState.value.copy(error = validation.errorMessage)
            return
        }
        viewModelScope.launch {
            if (_formState.value.id == 0L) {
                addWorkEntry(entry)
            } else {
                editWorkEntry(entry)
            }
            _formState.value = FormState() // Reset form
            onSuccess()
        }
    }

    fun loadEntry(entry: WorkEntry) {
        _formState.value = FormState(
            id = entry.id,
            date = entry.date,
            startTime = entry.startTime,
            endTime = entry.endTime,
            breakMinutes = entry.breakMinutes,
            task = entry.task,
            salary = entry.salary,
            salaryInput = CurrencyInputFormatter.formatForDisplay(entry.salary),
            isPaid = entry.isPaid,
            paidAmount = entry.paidAmount,
            paidAmountInput = CurrencyInputFormatter.formatForDisplay(entry.paidAmount),
            notes = entry.notes
        )
    }

    fun resetForm() {
        _formState.value = FormState()
    }

    data class FormState(
        val id: Long = 0L,
        val date: LocalDate = LocalDate.now(),
        val startTime: LocalTime = LocalTime.of(8, 0),
        val endTime: LocalTime = LocalTime.of(17, 0),
        val breakMinutes: Int = 0,
        val task: String = "",
        val salary: Long = 0L,
        val salaryInput: String = "",
        val paidAmount: Long = 0L,
        val paidAmountInput: String = "",
        val isPaid: Boolean = false,
        val notes: String = "",
        val error: String? = null
    )
}
