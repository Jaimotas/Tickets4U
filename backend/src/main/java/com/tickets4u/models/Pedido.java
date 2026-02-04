package com.tickets4u.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    @JsonBackReference
    private Evento evento;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String pago;

    @OneToMany(mappedBy = "pedido")
    @JsonIgnore
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "pedido")
    @JsonIgnore
    private List<Descuento> descuentos;

    public Pedido() {}

    public Pedido(Usuario cliente, Evento evento, BigDecimal total, String pago) {
        this.cliente = cliente;
        this.evento = evento;
        this.total = total;
        this.pago = pago;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getPago() { return pago; }
    public void setPago(String pago) { this.pago = pago; }
    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }
}
