package com.tickets4u.models;

import java.time.LocalDateTime;

public class EventoDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String ciudad;
    private String ubicacion;
    private String direccion;
    private Integer aforo;
    private String foto;
    private String categoria;

    private Integer ticketsDisponibles;
    private Integer ticketsVendidos;
    private Double ingresos;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getTicketsDisponibles() { return ticketsDisponibles; }
    public void setTicketsDisponibles(Integer ticketsDisponibles) { this.ticketsDisponibles = ticketsDisponibles; }

    public Integer getTicketsVendidos() { return ticketsVendidos; }
    public void setTicketsVendidos(Integer ticketsVendidos) { this.ticketsVendidos = ticketsVendidos; }

    public Double getIngresos() { return ingresos; }
    public void setIngresos(Double ingresos) { this.ingresos = ingresos; }
}
