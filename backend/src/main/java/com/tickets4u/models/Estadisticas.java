package com.tickets4u.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "estadisticas")
public class Estadisticas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(name = "tickets_disponibles")
    private int ticketsDisponibles;

    @Column(name = "tickets_vendidos")
    private int ticketsVendidos;

    private BigDecimal ingresos;

    // getters y setters
}
