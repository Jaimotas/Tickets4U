package com.tickets4u.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String pago;

    @OneToMany(mappedBy = "pedido")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "pedido")
    private List<Descuento> descuentos;

    // getters y setters
}
