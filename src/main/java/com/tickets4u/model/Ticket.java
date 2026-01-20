package com.tickets4u.model;

import jakarta.persistence.*;

@Entity
public class Ticket {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private long id;
 @OneToMany
 @JoinColumn(name="evento_id")
 private Evento evento;
 private String nombre;
 @Enumerated(EnumType.STRING)
 private TicketStatus estado;
 private String tipoEntrada;

 // getters y setters

 public enum TicketStatus {
     VALIDO, USADO, CANCELADO
 }
}
