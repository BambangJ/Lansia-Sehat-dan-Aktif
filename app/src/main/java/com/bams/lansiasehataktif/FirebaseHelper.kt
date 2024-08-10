package com.bams.lansiasehataktif

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ValueEventListener

class FirebaseHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun registerUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userId = it.uid
                    val newUser = User(userId, email, password)
                    database.child("users").child(userId).setValue(newUser)
                    callback(true, null)
                }
            } else {
                callback(false, task.exception?.message)
            }
        }
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }

/*    fun addPlace(place: Place) {
        val key = database.child("places").push().key ?: return
        place.id = key
        database.child("places").child(key).setValue(place)
    }*/

    fun getPlaces(callback: (List<Place>) -> Unit) {
        database.child("places").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val places = snapshot.children.mapNotNull { it.getValue(Place::class.java) }
                callback(places)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
