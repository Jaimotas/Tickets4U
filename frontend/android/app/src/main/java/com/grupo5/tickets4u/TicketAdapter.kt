package com.grupo5.tickets4u

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(
    private val context: Context,
    private val tickets: List<Ticket>
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    // Cambiamos los nombres aquí para que coincidan con tu XML
    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.tvTicketName)
        val txtFecha: TextView = view.findViewById(R.id.tvFechaEvento)
        val txtEstado: TextView = view.findViewById(R.id.tvTicketStatus)
        // Nota: He quitado txtTipoTicket porque no está en tu XML actual
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]

        // Usamos los datos del objeto Ticket que viene de Spring Boot
        holder.txtNombre.text = ticket.evento.nombre
        holder.txtFecha.text = "Fecha: ${ticket.evento.fechaInicio}"
        holder.txtEstado.text = "Estado: ${ticket.estado}"

        // Si quieres mostrar el tipo de entrada, deberías añadir un TextView
        // con el id tvTicketType al XML.
    }

    override fun getItemCount() = tickets.size
}