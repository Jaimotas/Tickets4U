package com.grupo5.tickets4u.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grupo5.tickets4u.R
import com.grupo5.tickets4u.model.TicketItem

class CartAdapter(
    private val viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var items: List<TicketItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_ticket, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<TicketItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtEvento: TextView = itemView.findViewById(R.id.txtEvento)
        private val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        private val btnMas: Button = itemView.findViewById(R.id.btnMas)
        private val btnMenos: Button = itemView.findViewById(R.id.btnMenos)

        fun bind(item: TicketItem) {
            txtEvento.text = item.nombreEvento
            txtPrecio.text = "${item.precio}€ x ${item.cantidad}"

            // Lógica: Ocultar botón "+" si ya hay 8 entradas
            if (item.cantidad >= 8) {
                btnMas.visibility = View.INVISIBLE
            } else {
                btnMas.visibility = View.VISIBLE
            }

            btnMas.setOnClickListener {
                if (item.cantidad < 8) viewModel.addItem(item)
            }

            btnMenos.setOnClickListener { viewModel.removeItem(item) }
        }
    }
}