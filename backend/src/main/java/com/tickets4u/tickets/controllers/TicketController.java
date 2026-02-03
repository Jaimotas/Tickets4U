package com.tickets4u.tickets.controllers;

import com.tickets4u.models.Ticket;
import com.tickets4u.tickets.repositories.TicketRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String qr) {
        System.out.println("🔍 DEBUG: Buscando QR: " + qr);
        
        Optional<Ticket> ticketOpt = ticketRepository.findByQr(qr);
        System.out.println("🔍 DEBUG: Ticket encontrado? " + ticketOpt.isPresent());
        
        if (ticketOpt.isEmpty()) {
            System.out.println("🔍 DEBUG: QR NO encontrado en BBDD");
            return ResponseEntity.badRequest().body("INVALIDO");
        }
        
        try {
            Ticket ticket = ticketOpt.get();
            System.out.println("🔍 DEBUG: Estado: " + ticket.getEstado());
            System.out.println("🔍 DEBUG: QR: " + ticket.getQr());
            
            return ResponseEntity.ok("VALIDO - Estado: " + ticket.getEstado());
            
        } catch (Exception e) {
            System.out.println("🔍 ERROR EXACTO: " + e.getClass().getSimpleName());
            System.out.println("🔍 MENSAJE: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("ERROR: " + e.getMessage());
        }
    }

    }

