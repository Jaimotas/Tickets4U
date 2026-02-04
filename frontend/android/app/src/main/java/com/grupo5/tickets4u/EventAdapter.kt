package com.grupo5.tickets4u

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class EventAdapter(
    private val events: List<Event>,
    private val onEdit: (Event) -> Unit,
    private val onDelete: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var isEditMode = false

    // Método para activar/desactivar el modo edición (lápiz)
    fun setEditMode(enabled: Boolean) {
        isEditMode = enabled
        notifyDataSetChanged()
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.event_name)
        val location: TextView = itemView.findViewById(R.id.event_location)
        val date: TextView = itemView.findViewById(R.id.event_date)
        val image: ImageView = itemView.findViewById(R.id.event_image)
        val trendingBadge: TextView = itemView.findViewById(R.id.event_trending_badge)
        val adminActions: LinearLayout = itemView.findViewById(R.id.layoutAdminActions)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEditEvent)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        // Asignación de textos básicos
        holder.name.text = event.nombre
        holder.location.text = event.ubicacion
        holder.date.text = event.fechaInicio

        // 🔹 Carga de imagen desde res/drawable usando el nombre de la DB
        val resId = getDrawableResIdFromName(holder.itemView.context, event.foto)
        Glide.with(holder.itemView.context)
            .load(resId)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(R.drawable.maluma)
            .into(holder.image)

        // Gestión de la etiqueta de tendencia
        holder.trendingBadge.visibility = if (event.categoria.equals("DESTACADO", ignoreCase = true))
            View.VISIBLE else View.GONE

        // Gestión de visibilidad de herramientas de Admin
        holder.adminActions.visibility = if (isEditMode) View.VISIBLE else View.GONE

        // Listeners de botones de edición y borrado
        holder.btnEdit.setOnClickListener { onEdit(event) }
        holder.btnDelete.setOnClickListener { onDelete(event) }

        // Listener para abrir los detalles del evento
        holder.itemView.setOnClickListener {
            if (!isEditMode) {
                val context = holder.itemView.context
                val intent = Intent(context, DatosDeEventoActivity::class.java).apply {
                    putExtra("EVENTO_ID", event.id)
                    putExtra("EVENTO_NOMBRE", event.nombre)
                    putExtra("EVENTO_UBICACION", event.ubicacion)
                    putExtra("EVENTO_FECHA", event.fechaInicio)
                    putExtra("EVENTO_FOTO", event.foto)
                    putExtra("EVENTO_DESCRIPCION", event.descripcion)
                }
                context.startActivity(intent)
            }
        }
    }

    fun getDrawableResIdFromName(context: Context, fotoNombre: String?): Int {
        if (fotoNombre.isNullOrBlank()) return R.drawable.maluma // fallback
        val nombreNormalizado = fotoNombre.substringBeforeLast('.').lowercase()
        val resId = context.resources.getIdentifier(nombreNormalizado, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.maluma
    }

    override fun getItemCount(): Int = events.size
}