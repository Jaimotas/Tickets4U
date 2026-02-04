package com.tickets4u.pedidos.controllers;

import com.tickets4u.models.Pedido;
import com.tickets4u.pedidos.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createPedido(@RequestBody Pedido pedido) {
        try {
            // Validación mínima antes de guardar
            if (pedido.getCliente() == null || pedido.getEvento() == null) {
                return ResponseEntity.badRequest().body("Falta cliente o evento en el pedido");
            }
            Pedido nuevoPedido = pedidoRepository.save(pedido);
            return ResponseEntity.ok(nuevoPedido);
        } catch (Exception e) {
            e.printStackTrace(); // Ver error en consola de IntelliJ
            return ResponseEntity.status(500).body("Error al crear pedido: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedidoRepository.delete(pedido);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}