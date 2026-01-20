package com.grupo5.tickets4u

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // <--- ASEGÚRATE DE ESTA IMPORTACIÓN


class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.event_name)
        val location: TextView = itemView.findViewById(R.id.event_location)
        val date: TextView = itemView.findViewById(R.id.event_date)
        val image: ImageView = itemView.findViewById(R.id.event_image)
        val trendingBadge: TextView = itemView.findViewById(R.id.event_trending_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.name.text = event.name
        holder.location.text = "${event.location} · ${event.city}"
        holder.date.text = event.date

        // USANDO GLIDE PARA CARGAR LA URL DEL BACKEND
        Glide.with(holder.itemView.context)
            .load(event.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery) // Imagen mientras carga
            .error(android.R.drawable.stat_notify_error)     // Imagen si falla
            .into(holder.image)

        holder.trendingBadge.visibility = if (position < 2) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = events.size
}
