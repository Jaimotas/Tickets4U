package com.tickets4u.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_admin", nullable = false)
    private Long idAdmin;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    private String ciudad;
    private String ubicacion;

    @Column(columnDefinition = "POINT")
    private String direccion;

    private Integer aforo;
    private String foto;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public enum Categoria {
        ACTUAL, DESTACADO, INTERNACIONAL;

        @JsonCreator
        public static Categoria fromString(String value) {
            if (value == null) return null;
            try {
                return Categoria.valueOf(value.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                return ACTUAL; // Valor por defecto si no coincide
            }
        }

        @JsonValue
        public String toValue() { return this.name(); }
    }

    @OneToMany(mappedBy = "evento")
    @JsonIgnore // Evita error 500 por recursión infinita
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "evento")
    @JsonIgnore // Evita error 500 por recursión infinita
    private List<Ticket> tickets;

    @OneToOne(mappedBy = "evento")
    private Estadisticas estadisticas;

    public Evento() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdAdmin() { return idAdmin; }
    public void setIdAdmin(Long idAdmin) { this.idAdmin = idAdmin; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Integer getAforo() { return aforo; }
    public void setAforo(Integer aforo) { this.aforo = aforo; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}