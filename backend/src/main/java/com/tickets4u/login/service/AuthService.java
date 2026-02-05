package com.tickets4u.login.service;

import com.tickets4u.login.repositories.UsuarioLoginRepository;
import com.tickets4u.models.Usuario;
import com.tickets4u.models.Usuario.Rol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioLoginRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    public LoginResponse login(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(usuario.getEmail());

        return new LoginResponse(token, usuario);
    }

    public static class LoginResponse {
        private String token;
        private Usuario usuario;

        public LoginResponse(String token, Usuario usuario) {
            this.token = token;
            this.usuario = usuario;
        }

        public String getToken() { return token; }
        public Usuario getUsuario() { return usuario; }
    }
}
