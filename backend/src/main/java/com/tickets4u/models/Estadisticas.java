package com.tickets4u.models;

import jakarta.persistence.*;

@Entity
@Table(name = "estadisticas")
public class Estadisticas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = false)
    private Evento evento;

    @Column(name = "tickets_disponibles")
    private Integer ticketsDisponibles;

    @Column(name = "tickets_vendidos")
    private Integer ticketsVendidos;

    @Column(name = "ingresos")
    private Double ingresos;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public Integer getTicketsDisponibles() { return ticketsDisponibles; }
    public void setTicketsDisponibles(Integer ticketsDisponibles) { this.ticketsDisponibles = ticketsDisponibles; }

    public Integer getTicketsVendidos() { return ticketsVendidos; }
    public void setTicketsVendidos(Integer ticketsVendidos) { this.ticketsVendidos = ticketsVendidos; }

    public Double getIngresos() { return ingresos; }
    public void setIngresos(Double ingresos) { this.ingresos = ingresos; }
}
