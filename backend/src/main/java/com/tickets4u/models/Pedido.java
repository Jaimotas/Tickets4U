package com.tickets4u.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;
    
    private BigDecimal total;
    private String pago;
    
    /*
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    */
    public Pedido() {}
    
    public Pedido(Long idCliente, Evento evento, BigDecimal total, String pago) {
        this.idCliente = idCliente;
        this.evento = evento;
        this.total = total;
        this.pago = pago;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getPago() { return pago; }
    public void setPago(String pago) { this.pago = pago; }
    
    /*
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    */
}
