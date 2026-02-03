package com.tickets4u.tickets.controllers;

import com.tickets4u.models.Ticket;
import com.tickets4u.tickets.repositories.TicketRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tickets4u.models.Evento;
import com.tickets4u.tickets.repositories.TicketRepository;
import com.tickets4u.events.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String qr) {
        System.out.println("🔍 DEBUG: Buscando QR: " + qr);
        
        Optional<Ticket> ticketOpt = ticketRepository.findByQr(qr);
        System.out.println("🔍 DEBUG: Ticket encontrado? " + ticketOpt.isPresent());
        
        if (ticketOpt.isEmpty()) {
            System.out.println("🔍 DEBUG: QR NO encontrado en BBDD");
            return ResponseEntity.badRequest().body("INVALIDO");
        }
        
        try {
            Ticket ticket = ticketOpt.get();
            System.out.println("🔍 DEBUG: Estado: " + ticket.getEstado());
            System.out.println("🔍 DEBUG: QR: " + ticket.getQr());
            
            return ResponseEntity.ok("VALIDO - Estado: " + ticket.getEstado());
            
        } catch (Exception e) {
            System.out.println("🔍 ERROR EXACTO: " + e.getClass().getSimpleName());
            System.out.println("🔍 MENSAJE: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("ERROR: " + e.getMessage());
        }
    }

    }


    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping("/cliente/{idCliente}")
    public List<Ticket> getTicketsByCliente(@PathVariable Integer idCliente) {
        return ticketRepository.findByIdCliente(idCliente);
    }

    /**
     * Crea tickets basados en los múltiples eventos que vienen del carrito.
     */
    @PostMapping("/crear-tickets")
    public List<Ticket> crearTickets(@RequestBody CrearTicketsRequest request) {
        List<Ticket> ticketsCreados = new ArrayList<>();

        // Iteramos sobre la lista de productos/eventos que vienen en la petición
        for (TicketItemRequest item : request.getItems()) {
            
            // Buscamos el evento real en la DB para cada item del carrito
            Evento evento = eventoRepository.findById(item.getIdEvento())
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado: " + item.getIdEvento()));

            // Generamos tantos tickets como cantidad haya pedido el usuario para este evento
            for (int i = 0; i < item.getCantidad(); i++) {
                Ticket ticket = new Ticket();
                ticket.setIdCliente(request.getIdCliente());
                ticket.setIdPedido(request.getIdPedido());
                ticket.setEvento(evento); // Asigna el evento correcto del item actual
                ticket.setTipoEntrada(item.getTipoEntrada());
                ticket.setEstado("ACTIVO");
                ticket.setQr(UUID.randomUUID().toString());
                
                ticketsCreados.add(ticketRepository.save(ticket));
            }
        }
        return ticketsCreados;
    }

    // --- DTOs para estructurar la recepción del carrito ---

    public static class CrearTicketsRequest {
        private Integer idCliente;
        private Long idPedido;
        private List<TicketItemRequest> items; // Ahora recibimos una lista de items

        public Integer getIdCliente() { return idCliente; }
        public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

        public Long getIdPedido() { return idPedido; }
        public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

        public List<TicketItemRequest> getItems() { return items; }
        public void setItems(List<TicketItemRequest> items) { this.items = items; }
    }

    public static class TicketItemRequest {
        private Long idEvento;
        private String tipoEntrada;
        private Integer cantidad;

        public Long getIdEvento() { return idEvento; }
        public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }

        public String getTipoEntrada() { return tipoEntrada; }
        public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
