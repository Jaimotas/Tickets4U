package com.tickets4u.tickets.repositories;

import com.tickets4u.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByQr(String qr);
    List<Ticket> findByClienteId(Long clienteId);
    List<Ticket> findByPedidoId(Long pedidoId);
    boolean existsByQr(String qr);
}
