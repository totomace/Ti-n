package com.example.tien.data.repository

import com.example.tien.data.dao.NoteDao
import com.example.tien.data.entity.toDomain
import com.example.tien.data.entity.toEntity
import com.example.tien.domain.model.Note
import com.example.tien.domain.repository.NoteRepository

class RoomNoteRepository(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun getAllNotes(): List<Note> =
        noteDao.getAllNotes().map { it.toDomain() }

    override suspend fun getNoteById(id: Long): Note? =
        noteDao.getNoteById(id)?.toDomain()

    override suspend fun addNote(note: Note): Long =
        noteDao.insertNote(note.toEntity())

    override suspend fun updateNote(note: Note) =
        noteDao.updateNote(note.toEntity())

    override suspend fun deleteNote(id: Long) =
        noteDao.deleteNoteById(id)
}
