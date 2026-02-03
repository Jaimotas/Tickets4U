package com.tickets4u.tickets.repositories;

import com.tickets4u.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Optional<Ticket> findByEventoIdAndClienteId(Integer eventoId, Integer clienteId);
    Optional<Ticket> findByQr(String qr);
}
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByIdCliente(Integer idCliente);
    List<Ticket> findByIdPedido(Long idPedido);
    Optional<Ticket> findByQr(String qr);
    boolean existsByQr(String qr);
}
