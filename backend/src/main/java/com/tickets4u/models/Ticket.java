package com.tickets4u.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"pedidos", "tickets", "contrasena"})
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonIgnoreProperties({"tickets", "descuentos"})
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    @JsonIgnoreProperties({"tickets", "pedidos", "estadisticas"})
    private Evento evento;

    @Column(nullable = false, unique = true)
    private String qr;

    @Column(name = "estado")
    private String estadoString;

    @Column(name = "tipo_entrada")
    @JsonProperty("tipo_entrada")
    private String tipoEntrada;

    public enum Estado {
        ACTIVO, USADO, CANCELADO
    }

    public Ticket() {}

    // Getters y Setters para Relaciones
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    // Manejo de Estado (String interno, Enum externo)
    @JsonProperty("estado")
    public String getEstado() {
        return (estadoString != null) ? estadoString.toUpperCase() : "ACTIVO";
    }

    public void setEstado(Estado estado) {
        this.estadoString = (estado != null) ? estado.name() : "ACTIVO";
    }

    // Getters y Setters Básicos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQr() { return qr; }
    public void setQr(String qr) { this.qr = qr; }
    public String getTipoEntrada() { return tipoEntrada; }
    public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }
}