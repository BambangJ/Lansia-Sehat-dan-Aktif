package com.bams.lansiasehataktif

import com.google.firebase.Timestamp
import java.util.Date

data class Reminder(
    var id: String? = null,
    var userId: String? = null,
    var title: String = "",
    var description: String = "",
    var time: Timestamp = Timestamp(Date())
)



