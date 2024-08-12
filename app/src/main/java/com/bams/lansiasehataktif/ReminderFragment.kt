package com.bams.lansiasehataktif

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import java.util.Calendar

class ReminderFragment : Fragment() {

    private lateinit var reminderViewModel: ReminderViewModel
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        // Inisialisasi Adapter dengan data kosong
        reminderAdapter = ReminderAdapter(emptyList())

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewremind)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = reminderAdapter

        // Setup SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshReminders()
        }

        // Observasi data LiveData dari ViewModel
        reminderViewModel.reminders.observe(viewLifecycleOwner, { reminders ->
            Log.d("ReminderFragment", "Received reminders: $reminders") // Debug log
            reminderAdapter.updateReminders(reminders)
            swipeRefreshLayout.isRefreshing = false // Stop refresh animation
        })

        // Listener untuk FloatingActionButton
        val addButton: FloatingActionButton = view.findViewById(R.id.fab_add_reminder)
        addButton.setOnClickListener {
            showAddReminderDialog()
        }
    }

    private fun refreshReminders() {
        reminderViewModel.loadReminders() // Load reminders again
        // No need to stop refresh animation here as it's handled by the observer
    }

    private fun showAddReminderDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Reminder")

        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.dialog_add_reminder, view as ViewGroup?, false)

        val inputTitle = viewInflated.findViewById<EditText>(R.id.input_reminder_title)
        val inputDescription = viewInflated.findViewById<EditText>(R.id.input_reminder_description)
        val inputTime = viewInflated.findViewById<TimePicker>(R.id.input_reminder_time)

        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            val title = inputTitle.text.toString()
            val description = inputDescription.text.toString()
            val hour = inputTime.hour
            val minute = inputTime.minute

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            // Create Reminder with Timestamp directly
            val reminder = Reminder(
                title = title,
                description = description,
                time = Timestamp(calendar.time)
            )

            // Add Reminder to ViewModel
            reminderViewModel.addReminder(reminder)
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
