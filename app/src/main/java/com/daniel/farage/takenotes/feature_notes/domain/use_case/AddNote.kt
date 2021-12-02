package com.daniel.farage.takenotes.feature_notes.domain.use_case

import com.daniel.farage.takenotes.feature_notes.domain.model.InvalidNoteException
import com.daniel.farage.takenotes.feature_notes.domain.model.Note
import com.daniel.farage.takenotes.feature_notes.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()){
            throw InvalidNoteException("O título não pode ser em branco")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("O conteúdo não pode ser em branco")
        }
        repository.insertNote(note)
    }

}