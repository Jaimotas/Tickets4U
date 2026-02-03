package com.tickets4u.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
package com.tickets4u.events.models;

import jakarta.persistence.*;

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
    private Long id;

    @Column(name = "tickets_disponibles")
    private Integer ticketsDisponibles;

    @Column(name = "tickets_vendidos")
    private Integer ticketsVendidos;

    @Column(name = "ingresos")
    private Double ingresos;

    @OneToOne
    @JoinColumn(name = "id_evento", referencedColumnName = "id")
    private Evento evento;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getTicketsDisponibles() { return ticketsDisponibles; }
    public void setTicketsDisponibles(Integer ticketsDisponibles) { this.ticketsDisponibles = ticketsDisponibles; }

    public Integer getTicketsVendidos() { return ticketsVendidos; }
    public void setTicketsVendidos(Integer ticketsVendidos) { this.ticketsVendidos = ticketsVendidos; }

    public Double getIngresos() { return ingresos; }
    public void setIngresos(Double ingresos) { this.ingresos = ingresos; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
}
