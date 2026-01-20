package com.grupo5.tickets4u

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.name.text = event.name
        holder.location.text = event.location
        holder.date.text = event.date
        holder.image.setImageResource(event.imageResId)

        // Mostrar u ocultar el badge según isTrending
        if (event.isTrending) {
            holder.trendingBadge.visibility = View.VISIBLE
        } else {
            holder.trendingBadge.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            // TODO: Ir a pantalla de detalle del evento
            // Click funciona pero NO sale nada visible
        }

    }
    }

