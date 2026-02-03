package com.grupo5.tickets4u.eventos.ui.cart

import com.grupo5.tickets4u.model.TicketItem

/**
 * Singleton encargado de gestionar la lista de entradas seleccionadas
 * durante la sesión de la aplicación.
 */
object CartManager {
    private val items = mutableListOf<TicketItem>()

    // Añade un item o incrementa su cantidad si ya existe
    fun addItem(newItem: TicketItem) {
        val existingItem = items.find { it.id == newItem.id }
        if (existingItem != null) {
            existingItem.cantidad += newItem.cantidad
        } else {
            items.add(newItem)
        }
    }

    // Retorna la lista actual de tickets
    fun getItems(): List<TicketItem> = items

    // Calcula el precio total de todos los tickets en el carrito
    fun getTotal(): Double {
        return items.sumOf { it.precio * it.cantidad }
    }

    // Limpia el carrito después de un pago exitoso
    fun clearCart() {
        items.clear()
    }
}