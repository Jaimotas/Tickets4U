package com.grupo5.tickets4u

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(
    private val context: Context,
    private val tickets: List<Ticket>,
    private val onItemClick: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.tvTicketName)
        val txtFecha: TextView = view.findViewById(R.id.tvFechaEvento)
        val txtEstado: TextView = view.findViewById(R.id.tvTicketStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]

        holder.txtNombre.text = ticket.evento.nombre

        // Formateo de fecha para la lista
        val fechaFormateada = formatearFechaCristiana(ticket.evento.fechaInicio)
        holder.txtFecha.text = "Fecha: $fechaFormateada"

        holder.txtEstado.text = "Estado: ${ticket.estado}"

        holder.itemView.setOnClickListener {
            onItemClick(ticket)
        }
    }

    override fun getItemCount() = tickets.size

    // Convierte ISO (2025-02-21T21:00:00) a 21/02/2025 21:00
    private fun formatearFechaCristiana(fechaRaw: String?): String {
        if (fechaRaw.isNullOrEmpty()) return "Sin fecha"
        return try {
            if (fechaRaw.contains("T")) {
                val partes = fechaRaw.split("T")
                val fechaPartes = partes[0].split("-") // [YYYY, MM, DD]
                val hora = partes[1].substring(0, 5)   // HH:mm
                "${fechaPartes[2]}/${fechaPartes[1]}/${fechaPartes[0]} $hora"
            } else {
                fechaRaw
            }
        } catch (e: Exception) {
            fechaRaw
        }
    }
}