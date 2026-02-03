	// PedidoController.java
	package com.tickets4u.pedidos.controllers;
	
	import com.tickets4u.models.Pedido;
	import com.tickets4u.pedidos.repositories.PedidoRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.web.bind.annotation.*;
	
	import java.util.List;
	import java.util.Optional;
	
	@RestController
	@RequestMapping("api/pedidos")
	public class PedidoController {
	
	    @Autowired
	    private PedidoRepository pedidoRepository;
	
	    // Obtener todos los pedidos
	    @GetMapping
	    public List<Pedido> getAllPedidos() {
	        return pedidoRepository.findAll();
	    }
	
	    // Obtener un pedido por su ID
	    @GetMapping("/{id}")
	    public Optional<Pedido> getPedidoById(@PathVariable Long id) {
	        return pedidoRepository.findById(id);
	    }
	
	    // Obtener pedidos por ID de evento
	    @GetMapping("/evento/{idEvento}")
	    public List<Pedido> getPedidosByEventoId(@PathVariable Long idEvento) {
	        return pedidoRepository.findByEventoId(idEvento);
	    }
	
	    // Crear un nuevo pedido
	    @PostMapping
	    public Pedido createPedido(@RequestBody Pedido pedido) {
	        return pedidoRepository.save(pedido);
	    }
	
	    // Actualizar un pedido existente
	    @PutMapping("/{id}")
	    public Pedido updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoDetails) {
	        Pedido pedido = pedidoRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
	
	        pedido.setIdCliente(pedidoDetails.getIdCliente());
	        pedido.setEvento(pedidoDetails.getEvento());
	        pedido.setTotal(pedidoDetails.getTotal());
	        pedido.setPago(pedidoDetails.getPago());
	
	        return pedidoRepository.save(pedido);
	    }
	
	    // Eliminar un pedido
	    @DeleteMapping("/{id}")
	    public void deletePedido(@PathVariable Long id) {
	        Pedido pedido = pedidoRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
	        pedidoRepository.delete(pedido);
	    }
	}
