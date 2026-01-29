package com.grupo5.tickets4u

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tickets4u.Ticket

class TicketAdapter(
    private val context: Context,
    private val tickets: List<Ticket>,
    private val eventos: Map<Int, Event> // id_evento -> Event
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvTicketName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvTicketStatus)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaEvento)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        val event = eventos[ticket.id_evento]

        holder.tvName.text = ticket.nombre
        holder.tvStatus.text = ticket.estado.name
        holder.tvFecha.text = event?.fechaInicio ?: "Fecha desconocida"

        holder.itemView.setOnClickListener {
            event?.let {
                val intent = Intent(context, TicketDetailActivity::class.java).apply {
                    putExtra("nombre", it.nombre)
                    putExtra("descripcion", it.descripcion)
                    putExtra("fecha_inicio", it.fechaInicio)
                    putExtra("fecha_fin", it.fechaFin)
                    putExtra("direccion", it.direccion)
                    putExtra("foto", it.foto)
                    putExtra("ciudad", it.ciudad)
                    putExtra("ubicacion", it.ubicacion)
                    putExtra("categoria", it.categoria)
                    putExtra("tipo_entrada", ticket.tipo_entrada)
                    putExtra("estado", ticket.estado.name)
                }
                context.startActivity(intent)
            }
        }
    }
}
