package com.tickets4u.login.DTO;

public class RegisterResponse {
    private Long id;
    private String nombreUsuario;
    private String email;

    public RegisterResponse(Long id, String nombreUsuario, String email) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getEmail() { return email; }
}
