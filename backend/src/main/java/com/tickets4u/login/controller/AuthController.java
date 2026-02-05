package com.tickets4u.login.controller;
import com.tickets4u.login.DTO.LoginRequest;
import com.tickets4u.login.DTO.RegisterRequest;
import com.tickets4u.login.DTO.RegisterResponse;
import com.tickets4u.login.repositories.UsuarioLoginRepository;
import com.tickets4u.login.service.AuthService.LoginResponse;
import com.tickets4u.login.service.JwtService;
import com.tickets4u.models.Usuario;
import com.tickets4u.models.Usuario.Rol;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioLoginRepository usuarioRepository;

    public AuthController(
            UsuarioLoginRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o contraseña incorrectos");
        }

        if (!passwordEncoder.matches(request.getContrasena(), user.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o contraseña incorrectos");
        }

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(new LoginResponse(token, user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El email ya está registrado");
        }

        Usuario user = new Usuario();
        user.setNombreUsuario(request.getNombreUsuario());
        user.setEmail(request.getEmail());
        user.setContrasena(passwordEncoder.encode(request.getContrasena()));
        user.setRol(Rol.cliente);

        Usuario savedUser = usuarioRepository.save(user);

        RegisterResponse response = new RegisterResponse(
                savedUser.getId(),
                savedUser.getNombreUsuario(),
                savedUser.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
