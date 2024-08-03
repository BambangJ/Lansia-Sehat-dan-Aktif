package com.bams.lansiasehataktif

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ServicesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var servicesAdapter: ServicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Example data
        val servicesList = listOf(
            ServiceItem("Health Check", "Free health check for seniors every Monday"),
            ServiceItem("Nutritional Advice", "Free nutritional advice every Wednesday"),
            ServiceItem("Physical Therapy", "Discounted physical therapy sessions every Friday")
        )

        servicesAdapter = ServicesAdapter(servicesList)
        recyclerView.adapter = servicesAdapter
    }
}

data class ServiceItem(val name: String, val description: String)

class ServicesAdapter(private val serviceList: List<ServiceItem>) :
    RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.serviceName)
        val description: TextView = itemView.findViewById(R.id.serviceDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val serviceItem = serviceList[position]
        holder.name.text = serviceItem.name
        holder.description.text = serviceItem.description
    }

    override fun getItemCount() = serviceList.size
}
