package com.example.tien.presentation.worklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tien.domain.model.WorkEntry
import com.example.tien.domain.usecase.GetWorkEntriesUseCase
import com.example.tien.domain.usecase.DeleteWorkEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkEntryListViewModel(
    private val getWorkEntries: GetWorkEntriesUseCase,
    private val deleteWorkEntry: DeleteWorkEntryUseCase
) : ViewModel() {
    private val _entries = MutableStateFlow<List<WorkEntry>>(emptyList())
    val entries: StateFlow<List<WorkEntry>> = _entries

    init {
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            _entries.value = getWorkEntries()
        }
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            deleteWorkEntry(id)
            loadEntries()
        }
    }
}
