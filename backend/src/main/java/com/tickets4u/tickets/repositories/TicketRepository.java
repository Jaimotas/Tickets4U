package com.tickets4u.tickets.repositories;

import com.tickets4u.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Buscar tickets por id del cliente
    List<Ticket> findByIdCliente(Integer idCliente);

    // Buscar tickets por id del evento
    List<Ticket> findByEventoId(Long idEvento);

    // Buscar tickets por id del pedido
    List<Ticket> findByIdPedido(Long idPedido);

    // Buscar ticket por código QR
    Optional<Ticket> findByQr(String qr);

    // Comprobar si existe un ticket con ese código QR
    boolean existsByQr(String qr);
}
