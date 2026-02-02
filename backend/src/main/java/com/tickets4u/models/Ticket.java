package com.tickets4u.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "id_pedido")
    private Long idPedido;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(unique = true)
    private String qr;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "tipo_entrada")
    private String tipoEntrada;

    @Column(name = "created_at")
    @Transient
    private LocalDateTime createdAt;
    
    public enum Estado {
        USADO, ACTIVO, CANCELADO
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (estado == null) {
            estado = Estado.ACTIVO;
        }
    }
    
    public Ticket() {}
    
    public Ticket(Integer idCliente, Long idPedido, Evento evento, String qr, String tipoEntrada) {
        this.idCliente = idCliente;
        this.idPedido = idPedido;
        this.evento = evento;
        this.qr = qr;
        this.tipoEntrada = tipoEntrada;
        this.estado = Estado.ACTIVO;
    }

    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    
    public String getQr() { return qr; }
    public void setQr(String qr) { this.qr = qr; }	
    
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    
    public String getTipoEntrada() { return tipoEntrada; }
    public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}