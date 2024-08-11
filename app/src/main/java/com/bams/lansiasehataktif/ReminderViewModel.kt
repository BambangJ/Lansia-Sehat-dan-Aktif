package com.bams.lansiasehataktif

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReminderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> get() = _reminders

    init {
        fetchReminders()
    }

    private fun fetchReminders() {
        db.collection("reminders")
            .orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val reminderList = snapshot.toObjects(Reminder::class.java)
                    _reminders.value = reminderList
                }
            }
    }

    fun addReminder(reminder: Reminder) {
        val newReminderRef = db.collection("reminders").document()
        reminder.id = newReminderRef.id
        newReminderRef.set(reminder)
    }
}
