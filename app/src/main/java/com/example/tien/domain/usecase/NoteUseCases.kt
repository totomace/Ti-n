package com.example.tien.domain.usecase

import com.example.tien.domain.model.Note
import com.example.tien.domain.repository.NoteRepository

class GetNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(): List<Note> = repository.getAllNotes()
}

class AddNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note): Long = repository.addNote(note)
}

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.updateNote(note)
}

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteNote(id)
}
