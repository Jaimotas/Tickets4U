package com.ticket4u.tickets.controllers;

import com.tickets4u.descuentos.models.Ticket;
import com.tickets4u.tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String qr) {
        try {
            String[] partes = qr.split(":");
            if (partes.length != 2) {
                return ResponseEntity.badRequest().body("{\"status\":\"INVALIDO\"}");
            }
            
            Integer eventoId = Integer.valueOf(partes[0]);
            Integer usuarioId = Integer.valueOf(partes[1]);
            
            // Busca activo
            Optional<Ticket> ticketActivo = ticketRepository.findByEventoIdAndUsuarioIdAndEstado(eventoId, usuarioId, "activo");
            
            if (ticketActivo.isEmpty()) {
                // Chequea usado/cancelado
                Optional<Ticket> cualquierTicket = ticketRepository.findByEventoIdAndUsuarioId(eventoId, usuarioId);
                if (cualquierTicket.isPresent()) {
                    String estado = cualquierTicket.get().getEstado();
                    return ResponseEntity.ok("{\"status\":\"" + estado.toUpperCase() + "\"}");
                }
                return ResponseEntity.badRequest().body("{\"status\":\"INVALIDO\"}");
            }
            
            // Marca usado
            Ticket ticket = ticketActivo.get();
            ticket.setEstado("usado");
            ticketRepository.save(ticket);
            
            return ResponseEntity.ok("{\"status\":\"USADO\"}");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"status\":\"ERROR\"}");
        }
    }
}
