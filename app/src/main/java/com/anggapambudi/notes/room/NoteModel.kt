package com.anggapambudi.notes.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val note: String
)
