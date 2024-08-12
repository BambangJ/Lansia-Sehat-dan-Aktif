package com.bams.lansiasehataktif

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReminderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _reminders = MutableLiveData<List<Reminder>>() // Gunakan MutableLiveData
    val reminders: LiveData<List<Reminder>> get() = _reminders // Expose sebagai LiveData

    init {
        loadReminders()
    }

    fun loadReminders() {
        // Fetch data dari Firebase Firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("reminders")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    return@addSnapshotListener
                }
                val reminderList = snapshot.toObjects(Reminder::class.java)
                _reminders.value = reminderList
            }
    }

    fun addReminder(reminder: Reminder) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val docRef = db.collection("reminders").document()
        reminder.id = docRef.id
        reminder.userId = userId
        docRef.set(reminder)
            .addOnSuccessListener {
                // Optionally, update local list after adding new reminder
                val currentReminders = _reminders.value?.toMutableList() ?: mutableListOf()
                currentReminders.add(reminder)
                _reminders.value = currentReminders
            }
            .addOnFailureListener {
                // Handle failure, if needed
            }
    }
}
