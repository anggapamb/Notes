package com.anggapambudi.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggapambudi.notes.adapter.NoteAdapter
import com.anggapambudi.notes.mode.Mode
import com.anggapambudi.notes.room.NoteDatabase
import com.anggapambudi.notes.room.NoteModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val database by lazy { NoteDatabase(this) }
    lateinit var noteAdapter: NoteAdapter
    private val displaySearch = ArrayList<NoteModel>()
    private val list = ArrayList<NoteModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarHome)

        setupListener()
        setupRecyclerview()

    }

    private fun setupRecyclerview() {
        noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.onAdapterListener {
            override fun onDelete(noteModel: NoteModel) {
                alertDelete(noteModel)
            }

            override fun onEdit(noteModel: NoteModel) {
                startActivity(Intent(this@MainActivity, EditActivity::class.java)
                        .putExtra("KEY_ID", noteModel.id)
                        .putExtra("ACCESS_VIEW", Mode.MODE_UPDATE)
                        .putExtra("KEY_TITLE", noteModel.title)
                        .putExtra("KEY_NOTE", noteModel.note)
                )
            }

        })
        tvRecyclerview.layoutManager = LinearLayoutManager(this)
        tvRecyclerview.adapter = noteAdapter
    }

    private fun setupListener() {
        btnPlus.onClick {
            val moceAdd = Intent(this@MainActivity, EditActivity::class.java)
                    .putExtra("ACCESS_VIEW", Mode.MODE_TAMBAH)
            startActivity(moceAdd)
        }
    }

    private fun alertDelete(noteModel: NoteModel) {
        alert("Apakah anda yakin ingin menghapapus ${noteModel.title}",
                "Konfirmasi") {
            yesButton {
                CoroutineScope(Dispatchers.IO).launch {
                    database.noteDao().deleteNote(noteModel)
                    loadData()
                }
                toast("${noteModel.title} is Deleted")
            }
            noButton {
                toast("Batal menghapus item")
            }
        }.show()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val getDataNote = database.noteDao().getNote()
            Log.d("MainActivity", "dataNoteResponse: $getDataNote")
            withContext(Dispatchers.Main) {
                noteAdapter.setData(getDataNote)
            }
        }
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//        menuInflater.inflate(R.menu.menu_toolbar_home, menu)
//        val searchIcon = menu!!.findItem(R.id.iconSearch)
//        val searchView = searchIcon.actionView as androidx.appcompat.widget.SearchView
//        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                if (query!!.isNotEmpty()) {
//
//                    displaySearch.clear()
//                    val search = query.toLowerCase(Locale.getDefault())
//                    list.forEach {
//
//                        if (it.title.toLowerCase(Locale.getDefault()).contains(search)) {
//
//                            displaySearch.add(it)
//
//                        }
//                    }
//
//                    tvRecyclerview.adapter!!.notifyDataSetChanged()
//                } else {
//
//                    displaySearch.clear()
//                    displaySearch.addAll(list)
//                    tvRecyclerview.adapter!!.notifyDataSetChanged()
//
//                }
//
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//
//            }
//
//        })
//
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////
////        when (item.itemId) {
////            R.id.iconSearch -> {
////                toast("hahaha")
////            }
////        }
//
//        return super.onOptionsItemSelected(item)
//    }

}