package com.bams.lansiasehataktif

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bams.lansiasehataktif.R
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class ReminderAdapter(private var reminders: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.bind(reminder)
    }

    override fun getItemCount() = reminders.size

    fun updateReminders(newReminders: List<Reminder>) {
        reminders = newReminders
        notifyDataSetChanged() // Notify the adapter that data has changed
    }

    inner class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        private val timeTextView: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(reminder: Reminder) {
            titleTextView.text = reminder.title
            descriptionTextView.text = reminder.description
            timeTextView.text = formatTimestamp(reminder.time)
        }

        private fun formatTimestamp(timestamp: Timestamp): String {
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            return sdf.format(timestamp.toDate())
        }
    }
}

