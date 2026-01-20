package com.tickets4u.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Pedido {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private long id;
 @OneToMany
 @JoinColumn(name="usuario_id")
 private long id_cliente;
 @OneToOne
 @JoinColumn(name="evento_id")
 private long id_evento;
 private int total;
 private String pago;
}
