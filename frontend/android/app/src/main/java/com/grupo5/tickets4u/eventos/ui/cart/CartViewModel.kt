package com.grupo5.tickets4u.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grupo5.tickets4u.model.TicketItem

class CartViewModel : ViewModel() {

    private val _items = MutableLiveData<MutableList<TicketItem>>(mutableListOf())
    val items: LiveData<MutableList<TicketItem>> = _items

    private val _total = MutableLiveData<Double>(0.0)
    val total: LiveData<Double> = _total

    fun addItem(item: TicketItem) {
        val currentList = _items.value ?: mutableListOf()
        val existingItem = currentList.find { it.id == item.id }

        if (existingItem != null) {
            // Límite estricto de 8
            if (existingItem.cantidad < 8) {
                existingItem.cantidad += 1
            }
        } else {
            currentList.add(item)
        }
        _items.value = currentList
        calculateTotal()
    }

    fun removeItem(item: TicketItem) {
        val currentList = _items.value ?: return
        val existingItem = currentList.find { it.id == item.id }

        if (existingItem != null) {
            if (existingItem.cantidad > 1) {
                existingItem.cantidad -= 1
            } else {
                currentList.remove(existingItem)
            }
            _items.value = currentList
            calculateTotal()
        }
    }

    private fun calculateTotal() {
        val totalValue = _items.value?.sumOf { it.precio * it.cantidad } ?: 0.0
        _total.value = totalValue
    }
}