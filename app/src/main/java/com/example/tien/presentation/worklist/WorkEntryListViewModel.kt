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
    private val _allEntries = MutableStateFlow<List<WorkEntry>>(emptyList())
    private val _entries = MutableStateFlow<List<WorkEntry>>(emptyList())
    val entries: StateFlow<List<WorkEntry>> = _entries
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    
    private val _filterType = MutableStateFlow(FilterType.ALL)
    val filterType: StateFlow<FilterType> = _filterType
    
    private val _sortType = MutableStateFlow(SortType.DATE_DESC)
    val sortType: StateFlow<SortType> = _sortType

    init {
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            _allEntries.value = getWorkEntries()
            applyFilters()
        }
    }
    
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFilters()
    }
    
    fun onFilterChange(filter: FilterType) {
        _filterType.value = filter
        applyFilters()
    }
    
    fun onSortChange(sort: SortType) {
        _sortType.value = sort
        applyFilters()
    }
    
    private fun applyFilters() {
        var filtered = _allEntries.value
        
        // Apply search
        if (_searchQuery.value.isNotEmpty()) {
            filtered = filtered.filter { 
                it.task.contains(_searchQuery.value, ignoreCase = true) ||
                it.notes.contains(_searchQuery.value, ignoreCase = true)
            }
        }
        
        // Apply filter type
        filtered = when (_filterType.value) {
            FilterType.ALL -> filtered
            FilterType.PAID -> filtered.filter { it.paidAmount >= it.salary }
            FilterType.UNPAID -> filtered.filter { it.paidAmount == 0L }
            FilterType.PARTIAL -> filtered.filter { it.paidAmount > 0 && it.paidAmount < it.salary }
        }
        
        // Apply sorting
        filtered = when (_sortType.value) {
            SortType.DATE_DESC -> filtered.sortedByDescending { it.date }
            SortType.DATE_ASC -> filtered.sortedBy { it.date }
            SortType.SALARY_DESC -> filtered.sortedByDescending { it.salary }
            SortType.SALARY_ASC -> filtered.sortedBy { it.salary }
            SortType.NAME_ASC -> filtered.sortedBy { it.task }
            SortType.NAME_DESC -> filtered.sortedByDescending { it.task }
        }
        
        _entries.value = filtered
    }

    fun deleteEntry(id: Long) {
        viewModelScope.launch {
            deleteWorkEntry(id)
            loadEntries()
        }
    }
}

enum class FilterType {
    ALL, PAID, UNPAID, PARTIAL
}

enum class SortType {
    DATE_DESC, DATE_ASC, SALARY_DESC, SALARY_ASC, NAME_ASC, NAME_DESC
}
