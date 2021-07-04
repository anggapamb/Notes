package com.anggapambudi.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.anggapambudi.notes.mode.Mode
import com.anggapambudi.notes.room.NoteDatabase
import com.anggapambudi.notes.room.NoteModel
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class EditActivity : AppCompatActivity() {

    private val database by lazy { NoteDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        setSupportActionBar(toolbarEdit)

        saveNote()
        updateNote()
        viewNote()

    }

    private fun updateNote() {
        btnUpdate.onClick {
            if (editTitle.text.trim().toString().isEmpty() || editNote.text.trim().toString().isEmpty()) {
                editTitle.error = "Field tidak boleh kosong!"
                editNote.error = "Field tidak boleh kosong"
            } else {

                val editTitle = editTitle.text.trim().toString()
                val editNote = editNote.text.trim().toString()
                val id = intent.getIntExtra("KEY_ID", 0)

                database.noteDao().updateNote(
                    NoteModel(id, editTitle, editNote)
                )
                finish()
            }
        }
    }

    private fun viewNote() {

        val title = intent.getStringExtra("KEY_TITLE")
        val note = intent.getStringExtra("KEY_NOTE")

        editTitle.setText(title)
        editNote.setText(note)


        when (intent.getIntExtra("ACCESS_VIEW", 0)) {
            Mode.MODE_BACA -> {
                btnSave.visibility = View.GONE
                btnUpdate.visibility = View.GONE
            }
            Mode.MODE_TAMBAH -> {
                btnUpdate.visibility = View.GONE
                btnSave.visibility = View.VISIBLE
            }
            Mode.MODE_UPDATE -> {
                btnSave.visibility = View.GONE
                btnUpdate.visibility = View.VISIBLE
            }
        }
    }

    private fun saveNote() {
        btnSave.onClick {
            if (editTitle.text.trim().toString().isEmpty() || editNote.text.trim().toString().isEmpty()) {
                editTitle.error = "Field tidak boleh kosong!"
                editNote.error = "Field tidak boleh kosong"
            } else {

                val editTitle = editTitle.text.trim().toString()
                val editNote = editNote.text.trim().toString()

                database.noteDao().addNote(
                    NoteModel(0, editTitle, editNote)
                )
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.iconShare -> {
                val t1 = "${editTitle.text.trim()} :\n${editNote.text.trim()}"
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, t1)
                startActivity(Intent.createChooser(shareIntent,"Share via"))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_edit_activity, menu)

        return super.onCreateOptionsMenu(menu)
    }
}