package com.bams.lansiasehataktif

import com.google.firebase.firestore.GeoPoint

data class Place(
    var id: String? = null,
    val name: String = "",
    val description: String = "",
    val geopoint: GeoPoint = GeoPoint(0.0, 0.0)
)
