package com.daniel.farage.takenotes.feature_notes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daniel.farage.takenotes.theme.*
import java.lang.Exception

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey
    val id: Int? = null
) {
    companion object {
        const val NOTE_ID = "noteId"
        val noteColors = listOf(
            RedOrange,
            LightGreen,
            Violet,
            BabyBlue,
            RedPink
        )
    }
}

class InvalidNoteException(message: String): Exception(message)
