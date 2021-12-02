package com.daniel.farage.takenotes.feature_notes.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.farage.takenotes.feature_notes.domain.model.Note
import com.daniel.farage.takenotes.feature_notes.domain.use_case.NoteUseCases
import com.daniel.farage.takenotes.feature_notes.domain.util.NoteOrder
import com.daniel.farage.takenotes.feature_notes.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> viewModelScope.launch {
                notesUseCases.deleteNote(event.note)
                recentlyDeletedNote = event.note
            }
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.order::class &&
                    state.value.noteOrder.orderType == event.order.orderType
                ) {
                    return
                }
                getNotes(event.order)
            }
            is NotesEvent.RestoreNote -> viewModelScope.launch {
                notesUseCases.addNote(recentlyDeletedNote ?: return@launch)
                recentlyDeletedNote = null
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value.copy(
                    isOrderSectionVisible = !_state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = notesUseCases.getNotes(noteOrder)
            .onEach { notes ->
            _state.value.copy(notes = notes, noteOrder = noteOrder)
        }.launchIn(viewModelScope)
    }

}