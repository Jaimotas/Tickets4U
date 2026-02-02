package com.tickets4u.tickets.controllers;

import com.tickets4u.models.Ticket;
import com.tickets4u.tickets.repositories.TicketRepository;
import com.tickets4u.models.Evento;
import com.tickets4u.events.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Ticket> getTicketById(@PathVariable Long id) {
        return ticketRepository.findById(id);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Ticket> getTicketsByCliente(@PathVariable Integer idCliente) {
        return ticketRepository.findByIdCliente(idCliente);
    }

    @GetMapping("/pedido/{idPedido}")
    public List<Ticket> getTicketsByPedido(@PathVariable Long idPedido) {
        return ticketRepository.findByIdPedido(idPedido);
    }

    @GetMapping("/qr/{qr}")
    public Optional<Ticket> getTicketByQr(@PathVariable String qr) {
        return ticketRepository.findByQr(qr);
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        // Generar QR único si no existe
        if (ticket.getQr() == null || ticket.getQr().isEmpty()) {
            String qr;
            do {
                qr = UUID.randomUUID().toString();
            } while (ticketRepository.existsByQr(qr));
            ticket.setQr(qr);
        }

        // Garantizar que estado sea ACTIVO
        ticket.setEstado(Ticket.Estado.ACTIVO);

        return ticketRepository.save(ticket);
    }

    @PostMapping("/crear-tickets")
    public List<Ticket> crearTickets(@RequestBody CrearTicketsRequest request) {
        List<Ticket> tickets = new java.util.ArrayList<>();

        // Buscar el evento correspondiente
        Evento evento = eventoRepository.findById(request.getIdEvento())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        for (int i = 0; i < request.getCantidad(); i++) {
            Ticket ticket = new Ticket();
            ticket.setIdCliente(request.getIdCliente());
            ticket.setIdPedido(request.getIdPedido());
            ticket.setEvento(evento);  // <-- Asignar la entidad Evento
            ticket.setTipoEntrada(request.getTipoEntrada());
            ticket.setEstado(Ticket.Estado.ACTIVO);

            // Generar QR único
            String qr;
            do {
                qr = UUID.randomUUID().toString();
            } while (ticketRepository.existsByQr(qr));
            ticket.setQr(qr);

            tickets.add(ticketRepository.save(ticket));
        }

        return tickets;
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setEstado(ticketDetails.getEstado());
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
    }

    // DTO para crear múltiples tickets
    public static class CrearTicketsRequest {
        private Integer idCliente;
        private Long idPedido;
        private Long idEvento;
        private String tipoEntrada;
        private Integer cantidad;

        public Integer getIdCliente() { return idCliente; }
        public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

        public Long getIdPedido() { return idPedido; }
        public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

        public Long getIdEvento() { return idEvento; }
        public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }

        public String getTipoEntrada() { return tipoEntrada; }
        public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
