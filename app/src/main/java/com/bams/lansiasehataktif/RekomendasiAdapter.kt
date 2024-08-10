package com.bams.lansiasehataktif

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RekomendasiAdapter(private val aktivitasList: List<Aktivitas>) :
    RecyclerView.Adapter<RekomendasiAdapter.RekomendasiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RekomendasiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aktivitas, parent, false)
        return RekomendasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: RekomendasiViewHolder, position: Int) {
        val aktivitas = aktivitasList[position]
        holder.bind(aktivitas)
    }

    override fun getItemCount() = aktivitasList.size

    inner class RekomendasiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val namaAktivitas: TextView = view.findViewById(R.id.tvNamaAktivitas)
        private val descAktivitas: TextView = view.findViewById(R.id.tvDescAktivitas)

        fun bind(aktivitas: Aktivitas) {
            namaAktivitas.text = aktivitas.nama_aktivitas
            descAktivitas.text = aktivitas.desc_aktivitas
        }
    }
}
