package com.bams.lansiasehataktif


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(
    private val places: List<Place>,
    private val onClick: (Place) -> Unit
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount() = places.size

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeName: TextView = itemView.findViewById(R.id.place_name)
        private val placeDescription: TextView = itemView.findViewById(R.id.place_description)

        fun bind(place: Place) {
            placeName.text = place.name
            placeDescription.text = place.description

            itemView.setOnClickListener { onClick(place) }
        }
    }
}

