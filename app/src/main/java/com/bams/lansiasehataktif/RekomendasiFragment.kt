package com.bams.lansiasehataktif

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RekomendasiFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rekomendasiList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_aktivitas, container, false)
        rekomendasiList = view.findViewById(R.id.recyclerViewAktivitas)
        rekomendasiList.layoutManager = LinearLayoutManager(context)

        db = FirebaseFirestore.getInstance()

        fetchRekomendasiAktivitas()

        return view
    }

    private fun fetchRekomendasiAktivitas() {
        db.collection("aktivitas").get()
            .addOnSuccessListener { documents ->
                val aktivitasList = mutableListOf<Aktivitas>()
                for (document in documents) {
                    val aktivitas = document.toObject(Aktivitas::class.java)
                    aktivitasList.add(aktivitas)
                }
                rekomendasiList.adapter = RekomendasiAdapter(aktivitasList)
            }
            .addOnFailureListener { e ->
                Log.e("RekomendasiFragment", "Failed to fetch aktivitas", e)
            }
    }
}
