package com.daniel.farage.takenotes.feature_notes.data.repository

import com.daniel.farage.takenotes.feature_notes.data.data_source.NoteDao
import com.daniel.farage.takenotes.feature_notes.domain.model.Note
import com.daniel.farage.takenotes.feature_notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao
): NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}