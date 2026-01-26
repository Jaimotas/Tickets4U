package com.grupo5.tickets4u

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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

        // Mapeo con campos del Backend
        holder.name.text = event.nombre
        holder.location.text = event.ubicacion
        holder.date.text = event.fechaInicio

        Glide.with(holder.itemView.context)
            .load(event.foto) // URL de la imagen del servidor
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.image)

        holder.trendingBadge.visibility = if (event.categoria == "DESTACADO") View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PaginaCompraActivity::class.java).apply {
                putExtra("EVENTO_ID", event.id)
                putExtra("EVENTO_NOMBRE", event.nombre)
                putExtra("EVENTO_UBICACION", event.ubicacion)
                putExtra("EVENTO_FECHA", event.fechaInicio)
                putExtra("EVENTO_FOTO", event.foto)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = events.size
}