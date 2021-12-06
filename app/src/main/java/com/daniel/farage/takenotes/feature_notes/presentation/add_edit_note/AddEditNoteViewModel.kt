package com.daniel.farage.takenotes.feature_notes.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.farage.takenotes.feature_notes.domain.model.InvalidNoteException
import com.daniel.farage.takenotes.feature_notes.domain.model.Note
import com.daniel.farage.takenotes.feature_notes.domain.model.Note.Companion.NOTE_ID
import com.daniel.farage.takenotes.feature_notes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextfieldState(hintText = "Insira um título"))
    val noteTitle: State<NoteTextfieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextfieldState(hintText = "Insira sua nota"))
    val noteContent: State<NoteTextfieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>(NOTE_ID)?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = _noteTitle.value.copy(text = event.value)
            }
            is AddEditNoteEvent.ChangeFocusTitle -> {
                _noteTitle.value =
                    _noteTitle.value.copy(
                        isHintVisible = !event.focusState.isFocused
                                && noteTitle.value.text.isBlank()
                    )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(text = event.value)
            }
            is AddEditNoteEvent.ChangeFocusContent -> {
                _noteContent.value =
                    _noteContent.value.copy(
                        isHintVisible = !event.focusState.isFocused
                                && noteContent.value.text.isBlank()
                    )
            }
            is AddEditNoteEvent.ChangeColor -> _noteColor.value = event.color
            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (exception: InvalidNoteException) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            exception.message ?: "Erro ao salvar nota"
                        ))
                    }
                }
            }
        }
    }

}