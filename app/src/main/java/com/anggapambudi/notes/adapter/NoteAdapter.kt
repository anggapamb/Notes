package com.anggapambudi.notes.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anggapambudi.notes.EditActivity
import com.anggapambudi.notes.R
import com.anggapambudi.notes.mode.Mode
import com.anggapambudi.notes.room.NoteModel
import kotlinx.android.synthetic.main.item_notes.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class NoteAdapter(private val item: ArrayList<NoteModel>, private val listener: onAdapterListener) :
        RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
    )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteModel = item[position]
        holder.itemView.tvTitle.text = noteModel.title
        holder.itemView.tvNote.text = noteModel.note

        holder.itemView.btnDelete.onClick {
            listener.onDelete( noteModel )
        }
        holder.itemView.btnEdit.onClick {
            listener.onEdit( noteModel )
        }
        holder.itemView.onClick {
            val moveRead = Intent(holder.itemView.context, EditActivity::class.java)
                .putExtra("KEY_TITLE", noteModel.title)
                .putExtra("KEY_NOTE", noteModel.note)
                .putExtra("ACCESS_VIEW", Mode.MODE_BACA)
            holder.itemView.context.startActivity(moveRead)
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    fun setData(data: List<NoteModel>) {
        item.clear()
        item.addAll(data)
        notifyDataSetChanged()
    }

    interface onAdapterListener {
        fun onDelete(noteModel: NoteModel)
        fun onEdit(noteModel: NoteModel)
    }

}