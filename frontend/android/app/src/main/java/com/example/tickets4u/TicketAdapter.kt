package com.example.tickets4u

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(
    private val context: Context,
    private val tickets: List<Ticket>,
    private val eventos: Map<Int, Evento> // mapa id_evento -> Evento
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvTicketName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvTicketStatus)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        val evento = eventos[ticket.id_evento]

        holder.tvName.text = ticket.nombre
        holder.tvStatus.text = ticket.estado.name

        // Mostrar fecha de inicio como string legible
        holder.tvFecha.text = evento?.fecha_inicio?.toLocalDate()?.toString() ?: "Fecha desconocida"

        holder.itemView.setOnClickListener {
            evento?.let {
                val intent = Intent(context, TicketDetailActivity::class.java).apply {
                    putExtra("nombre", it.nombre)
                    putExtra("descripcion", it.descripcion)
                    putExtra("fecha_inicio", it.fecha_inicio.toString())
                    putExtra("fecha_fin", it.fecha_fin.toString())
                    putExtra("direccion", it.direccion)
                    putExtra("foto", it.foto)
                    putExtra("qr", it.qr)
                    putExtra("tipo_entrada", ticket.tipo_entrada)
                    putExtra("estado", ticket.estado.name)
                }
                context.startActivity(intent)
            }
        }
    }
}
