package com.bams.lansiasehataktif

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bams.lansiasehataktif.R

class ReminderFragment : Fragment() {

    private lateinit var reminderViewModel: ReminderViewModel
    private lateinit var reminderAdapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderViewModel = ViewModelProvider(this)[ReminderViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewremind)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        reminderViewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            reminderAdapter = ReminderAdapter(reminders)
            recyclerView.adapter = reminderAdapter
        }
    }
}
