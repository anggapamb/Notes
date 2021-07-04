package com.anggapambudi.notes.room

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(noteModel: NoteModel)

    @Update
    suspend fun updateNote(noteModel: NoteModel)

    @Delete
    suspend fun deleteNote(noteModel: NoteModel)

    @Query("SELECT * FROM NoteModel")
    suspend fun getNote(): List<NoteModel>

}