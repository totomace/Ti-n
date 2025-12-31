package com.example.tien.presentation.workentry.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.tien.data.WorkEntryDatabase
import com.example.tien.data.repository.RoomWorkEntryRepository
import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.usecase.AddWorkEntryUseCase
import com.example.tien.domain.usecase.EditWorkEntryUseCase
import com.example.tien.domain.usecase.ValidateWorkEntryUseCase
import com.example.tien.presentation.workentry.WorkEntryFormViewModel

@Composable
fun WorkEntryScreen(
    entryToEdit: WorkEntry? = null,
    onViewHistory: () -> Unit
) {
    val context = LocalContext.current.applicationContext as Application
    val db = remember(context) { WorkEntryDatabase.getInstance(context) }
    val repository = remember(db) { RoomWorkEntryRepository(db.workEntryDao()) }
    val viewModel: WorkEntryFormViewModel = viewModel(
        key = "form_vm_${entryToEdit?.id ?: 0}",
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return WorkEntryFormViewModel(
                    addWorkEntry = AddWorkEntryUseCase(repository),
                    editWorkEntry = EditWorkEntryUseCase(repository),
                    validateWorkEntry = ValidateWorkEntryUseCase()
                ) as T
            }
        }
    )
    
    LaunchedEffect(entryToEdit) {
        if (entryToEdit != null) {
            viewModel.loadEntry(entryToEdit)
        } else {
            viewModel.resetForm()
        }
    }
    
    val state = viewModel.formState.collectAsState().value
    WorkEntryForm(
        state = state,
        onDateChange = viewModel::onDateChange,
        onStartTimeChange = viewModel::onStartTimeChange,
        onEndTimeChange = viewModel::onEndTimeChange,
        onBreakMinutesChange = viewModel::onBreakMinutesChange,
        onTaskChange = viewModel::onTaskChange,
        onSalaryChange = viewModel::onSalaryChange,
        onPaidAmountChange = viewModel::onPaidAmountChange,
        onNotesChange = viewModel::onNotesChange,
        onSubmit = { viewModel.submit(onSuccess = onViewHistory) },
        onViewHistory = onViewHistory
    )
}
