// PedidoRepository.java
package com.tickets4u.pedidos.repositories;

import com.tickets4u.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Buscar pedidos por el id del evento
    List<Pedido> findByEventoId(Long idEvento);
}
