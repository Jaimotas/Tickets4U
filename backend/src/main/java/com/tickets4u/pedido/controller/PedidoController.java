package com.tickets4u.pedido.controller;

import com.tickets4u.models.Evento;
import com.tickets4u.models.Pedido;
import com.tickets4u.models.Usuario;
import com.tickets4u.pedido.repository.PedidoRepository;
import com.tickets4u.login.repository.UsuarioLoginRepository;
import com.tickets4u.events.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired private JavaMailSender mailSender;
    @Autowired private UsuarioLoginRepository usuarioRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private EventoRepository eventoRepository;

    @PostMapping("/pedido/confirmar")
    public ResponseEntity<?> confirmarPedido(
            @RequestParam double total,
            @RequestParam Long idEvento  // 👈 CAMBIO: recibir solo el ID
    ) {
        try {
            // 1) Obtener el email del usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("CONFIRMAR_PEDIDO: Usuario no autenticado");
                return ResponseEntity.status(401).body("Usuario no autenticado");
            }
            
            String email = authentication.getName();
            System.out.println("CONFIRMAR_PEDIDO: email=" + email + ", total=" + total + ", idEvento=" + idEvento);

            // 2) Validación del total
            if (total <= 0) {
                System.out.println("CONFIRMAR_PEDIDO: total inválido: " + total);
                return ResponseEntity.badRequest().body("Total inválido");
            }

            // 3) Buscar usuario
            Optional<Usuario> userOpt = usuarioRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                System.out.println("CONFIRMAR_PEDIDO: Usuario no encontrado");
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }
            Usuario usuario = userOpt.get();

            // 4) Buscar evento
            Optional<Evento> eventoOpt = eventoRepository.findById(idEvento);
            if (eventoOpt.isEmpty()) {
                System.out.println("CONFIRMAR_PEDIDO: Evento no encontrado");
                return ResponseEntity.badRequest().body("Evento no encontrado");
            }
            Evento evento = eventoOpt.get();

            // 5) Guardar pedido
            Pedido pedido = new Pedido();
            pedido.setCliente(usuario);
            pedido.setEvento(evento);
            pedido.setTotal(BigDecimal.valueOf(total));
            pedido.setPago("TARJETA");
            pedido = pedidoRepository.save(pedido);

            System.out.println("CONFIRMAR_PEDIDO: pedido guardado. idPedido=" + pedido.getId());

            // 6) Enviar email
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setFrom("tickets4u.noreply@gmail.com");
            msg.setSubject("Compra confirmada - Tickets4U");
            msg.setText(
                    "Hola " + safe(usuario.getNombreUsuario()) + ",\n\n" +
                    "Tu compra se ha efectuado con éxito.\n" +
                    "Pedido ID: " + pedido.getId() + "\n" +
                    "Evento: " + evento.getNombre() + "\n" +  // 👈 Mejor mostrar el nombre
                    "Total: " + String.format("%.2f", total) + "€\n\n" +
                    "Gracias por usar Tickets4U."
            );

            mailSender.send(msg);
            System.out.println("CONFIRMAR_PEDIDO: email enviado a " + email);

            return ResponseEntity.ok("OK: pedido=" + pedido.getId());

        } catch (MailException me) {
            System.out.println("CONFIRMAR_PEDIDO: MailException -> " + me.getMessage());
            me.printStackTrace();
            return ResponseEntity.status(500).body("Error enviando email: " + me.getMessage());

        } catch (Exception e) {
            System.out.println("CONFIRMAR_PEDIDO: Exception -> " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}