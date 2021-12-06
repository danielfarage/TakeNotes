package com.daniel.farage.takenotes.feature_notes.presentation.add_edit_note

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object SaveNote: UiEvent()
}
