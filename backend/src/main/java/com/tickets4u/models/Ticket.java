package com.tickets4u.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonIgnoreProperties("tickets")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    @JsonIgnoreProperties({"tickets", "pedidos"})
    private Evento evento;

    @Column(nullable = false, unique = true)
    private String qr;

    // TRUCO: Mapeamos como String para que Hibernate no valide minúsculas/mayúsculas
    @Column(name = "estado")
    private String estadoString;

    @Column(name = "tipo_entrada")
    private String tipoEntrada;

    public enum Estado {
        ACTIVO, USADO, CANCELADO;

        @JsonCreator
        public static Estado fromString(String value) {
            if (value == null) return null;
            return Estado.valueOf(value.toUpperCase().trim());
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }

    public Ticket() {}

    // GETTER para el Enum (Aquí hacemos la conversión mágica)
    public Estado getEstado() {
        if (this.estadoString == null) return null;
        try {
            return Estado.valueOf(this.estadoString.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return Estado.ACTIVO; // Valor por defecto si hay algo raro
        }
    }

    // SETTER para el Enum
    public void setEstado(Estado estado) {
        this.estadoString = (estado == null) ? null : estado.name();
    }

    // Getters y Setters estándar
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public String getQr() { return qr; }
    public void setQr(String qr) { this.qr = qr; }
    public String getTipoEntrada() { return tipoEntrada; }
    public void setTipoEntrada(String tipoEntrada) { this.tipoEntrada = tipoEntrada; }
}