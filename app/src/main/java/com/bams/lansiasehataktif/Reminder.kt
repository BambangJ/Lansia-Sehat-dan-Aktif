package com.bams.lansiasehataktif

import com.google.firebase.Timestamp

data class Reminder(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val time: Timestamp = Timestamp.now(),
    val userId: String = ""
)



