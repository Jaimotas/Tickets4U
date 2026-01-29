package com.tickets4u.tickets.repositories;

import com.tickets4u.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Optional<Ticket> findByEventoIdAndUsuarioId(Integer eventoId, Integer usuarioId);
    Optional<Ticket> findByEventoIdAndUsuarioIdAndEstado(Integer eventoId, Integer usuarioId, String estado);
}
