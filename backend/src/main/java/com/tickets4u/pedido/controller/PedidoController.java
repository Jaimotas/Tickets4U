package com.tickets4u.pedido.controller;

import com.tickets4u.models.Evento;
import com.tickets4u.models.Pedido;
import com.tickets4u.models.Usuario;
import com.tickets4u.pedido.repository.PedidoRepository;
import com.tickets4u.events.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired private JavaMailSender mailSender;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PedidoRepository pedidoRepository;

    /**
     * POST /api/pedido/confirmar
     * Content-Type: application/x-www-form-urlencoded
     * Params: email, total, idEvento
     */
    @PostMapping("/pedido/confirmar")
    public ResponseEntity<?> confirmarPedido(
            @RequestParam String email,
            @RequestParam double total,
            @RequestParam Evento evento
    ) {
        System.out.println("CONFIRMAR_PEDIDO: entro. email=" + email + ", total=" + total + ", Evento=" + evento);

        try {
            // 1) Validaciones simples
            if (email == null || email.trim().isEmpty()) {
                System.out.println("CONFIRMAR_PEDIDO: email vacío");
                return ResponseEntity.badRequest().body("Email vacío");
            }
            if (total <= 0) {
                System.out.println("CONFIRMAR_PEDIDO: total inválido: " + total);
                return ResponseEntity.badRequest().body("Total inválido");
            }

            // 2) Buscar usuario por email (mejor que findAll)
            Optional<Usuario> userOpt = usuarioRepository.findByEmail(email.trim());
            if (userOpt.isEmpty()) {
                System.out.println("CONFIRMAR_PEDIDO: Usuario no encontrado con email=" + email);
                return ResponseEntity.badRequest().body("Usuario no encontrado: " + email);
            }
            Usuario usuario = userOpt.get();

            // 3) Guardar pedido
            Pedido pedido = new Pedido();
            pedido.setCliente(usuario);
            pedido.setEvento(evento);
            pedido.setTotal(BigDecimal.valueOf(total));
            pedido.setPago("TARJETA");
            pedido = pedidoRepository.save(pedido);

            System.out.println("CONFIRMAR_PEDIDO: pedido guardado. idPedido=" + pedido.getId());

            // 4) Construi email
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email.trim());
            msg.setFrom("tickets4u.noreply@gmail.com"); // el mismo que spring.mail.username
            msg.setSubject("Compra confirmada - Tickets4U");
            msg.setText(
                    "Hola " + safe(usuario.getNombreUsuario()) + ",\n\n" +
                    "Tu compra se ha efectuado con éxito.\n" +
                    "Pedido ID: " + pedido.getId() + "\n" +
                    "Evento ID: " + evento + "\n" +
                    "Total: " + String.format("%.2f", total) + "€\n\n" +	
                    "Gracias por usar Tickets4U."
            );

            // 5) Enviar email
            System.out.println("CONFIRMAR_PEDIDO: voy a enviar email a " + email);
            mailSender.send(msg);	
            System.out.println("CONFIRMAR_PEDIDO: email enviado (send() ejecutado)");

            return ResponseEntity.ok("OK: pedido=" + pedido.getId() + " email enviado");

        } catch (MailException me) {
            // errores SMTP, auth, etc.
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