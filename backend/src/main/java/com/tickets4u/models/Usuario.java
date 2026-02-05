package com.tickets4u.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre_usuario", unique = true)
    private String nombreUsuario;
    
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String contrasena;
    
    // TRUCO: Lo mapeamos como String en la base de datos para evitar el error de Hibernate
    @Column(name = "rol")
    private String rolString;
    
    public enum Rol {
       admin,cliente
    }
    @JsonCreator
    public static Rol fromString(String value) {
        if (value == null) return null;
        return Rol.valueOf(value.toUpperCase().trim());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

    public Usuario() {}
    
    public Usuario(String nombreUsuario, String email, String contrasena, Rol rol) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasena = contrasena;
        setRol(rol); // Usamos el setter para guardar como String
    }
    
    // Getters y Setters modificados para manejar el Enum
    public Rol getRol() {
        if (this.rolString == null) return null;
        try {
            // Aquí forzamos las mayúsculas al leer de la base de datos
            return Rol.valueOf(this.rolString.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setRol(Rol rol) {
        this.rolString = (rol == null) ? null : rol.name();
    }

    // Getters y Setters estándar
    public Long getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}