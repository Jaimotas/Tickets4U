package com.tickets4u.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(nullable = false, unique = true)
    private String qr;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "tipo_entrada")
    private String tipoEntrada;

    public enum Estado {
        usado, activo, cancelado  // ← FALTA ;
    }  // ← PUNTO Y COMA AQUÍ
    
    // GETTERS
    public Integer getId() { return id; }
    public Usuario getCliente() { return cliente; }
    public Pedido getPedido() { return pedido; }
    public Evento getEvento() { return evento; }
    public String getQr() { return qr; }
    public Estado getEstado() { return estado; }
    public String getTipoEntrada() { return tipoEntrada; }
    
    // ← SETTERS FALTANTES
    public void setId(Integer id) { this.id = id; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public void setQr(String qr) { this.qr = qr; }
    
    public void setEstado(Estado estado) {  // ← ESTE ERA EL QUE FALTABA
        this.estado = estado;
    }
    
    public void setTipoEntrada(String tipoEntrada) { 
        this.tipoEntrada = tipoEntrada; 
    }
}
