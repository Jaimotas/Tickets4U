package com.tickets4u.pedido.repositories;

import com.tickets4u.models.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Buscar pedidos por el id del evento
    List<Pedido> findByEventoId(Long idEvento);
}