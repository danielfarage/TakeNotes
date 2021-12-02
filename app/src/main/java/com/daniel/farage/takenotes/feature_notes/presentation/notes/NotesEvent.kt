package com.daniel.farage.takenotes.feature_notes.presentation.notes

import com.daniel.farage.takenotes.feature_notes.domain.model.Note
import com.daniel.farage.takenotes.feature_notes.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val order: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}
