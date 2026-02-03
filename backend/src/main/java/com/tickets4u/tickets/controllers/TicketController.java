package com.tickets4u.tickets.controllers;

import com.tickets4u.models.Ticket;
import com.tickets4u.models.Evento;
import com.tickets4u.models.Usuario;
import com.tickets4u.models.Pedido;
import com.tickets4u.tickets.repositories.TicketRepository;
import com.tickets4u.events.repositories.EventoRepository;
import com.tickets4u.events.repositories.UsuarioRepository;
import com.tickets4u.pedidos.repositories.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String qr) {
        Optional<Ticket> ticketOpt = ticketRepository.findByQr(qr);
        if (ticketOpt.isEmpty()) return ResponseEntity.badRequest().body("INVALIDO");
        Ticket ticket = ticketOpt.get();
        return ResponseEntity.ok("VALIDO - Estado: " + ticket.getEstado());
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Ticket> getTicketsByCliente(@PathVariable Long idCliente) {
        return ticketRepository.findByClienteId(idCliente);
    }

    @PostMapping("/crear-tickets")
    public List<Ticket> crearTickets(@RequestBody CrearTicketsRequest request) {
        Usuario cliente = usuarioRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Pedido pedido = pedidoRepository.findById(request.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<Ticket> ticketsCreados = new ArrayList<>();
        for (TicketItemRequest item : request.getItems()) {
            Evento evento = eventoRepository.findById(item.getIdEvento())
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
            for (int i = 0; i < item.getCantidad(); i++) {
                Ticket ticket = new Ticket();
                ticket.setCliente(cliente);
                ticket.setPedido(pedido);
                ticket.setEvento(evento);
                ticket.setTipoEntrada(item.getTipoEntrada());
                ticket.setEstado(Ticket.Estado.ACTIVO);
                ticket.setQr(UUID.randomUUID().toString());
                ticketsCreados.add(ticketRepository.save(ticket));
            }
        }
        return ticketsCreados;
    }

    // ===== DTOs =====
    public static class CrearTicketsRequest {
        private Long idCliente;
        private Long idPedido;
        private List<TicketItemRequest> items;

        public Long getIdCliente() { return idCliente; }
        public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
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
