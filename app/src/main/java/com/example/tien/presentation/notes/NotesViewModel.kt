package com.example.tien.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tien.domain.model.Note
import com.example.tien.domain.usecase.AddNoteUseCase
import com.example.tien.domain.usecase.DeleteNoteUseCase
import com.example.tien.domain.usecase.GetNotesUseCase
import com.example.tien.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NotesViewModel(
    private val getNotes: GetNotesUseCase,
    private val addNote: AddNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase
) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = getNotes()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredNotes(): List<Note> {
        val query = _searchQuery.value
        return if (query.isBlank()) {
            _notes.value
        } else {
            _notes.value.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.content.contains(query, ignoreCase = true)
            }
        }
    }

    fun saveNote(title: String, content: String, noteId: Long? = null, onSuccess: () -> Unit = {}) {
        if (title.isBlank() || content.isBlank()) return
        
        viewModelScope.launch {
            if (noteId == null || noteId == 0L) {
                addNote(
                    Note(
                        title = title,
                        content = content,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                )
            } else {
                val existingNote = _notes.value.find { it.id == noteId }
                if (existingNote != null) {
                    updateNote(
                        existingNote.copy(
                            title = title,
                            content = content,
                            updatedAt = LocalDateTime.now()
                        )
                    )
                }
            }
            loadNotes()
            onSuccess()
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            deleteNote(id)
            loadNotes()
        }
    }
}
