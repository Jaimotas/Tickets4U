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
        usado, activo, cancelado
    }
    
    // getters y setters

	public Integer getId() {
		return id;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public Evento getEvento() {
		return evento;
	}

	public String getQr() {
		return qr;
	}

	public Estado getEstado() {
		return estado;
	}

	public String getTipoEntrada() {
		return tipoEntrada;
	}

  
    
}