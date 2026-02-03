package com.tickets4u.events.controllers;

import com.tickets4u.events.repositories.EventoRepository;
import com.tickets4u.models.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
public class EventoController {
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @GetMapping
    public List<Evento> getAllEventos() {
        return eventoRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        return eventoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Evento> createEvento(@RequestBody Evento evento) {
        try {
            return ResponseEntity.ok(eventoRepository.save(evento));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Evento> updateEvento(@PathVariable Long id, @RequestBody Evento eventoDetails) {
        return eventoRepository.findById(id).map(evento -> {
            evento.setNombre(eventoDetails.getNombre());
            evento.setDescripcion(eventoDetails.getDescripcion());
            evento.setFechaInicio(eventoDetails.getFechaInicio());
            evento.setFechaFin(eventoDetails.getFechaFin());
            evento.setCiudad(eventoDetails.getCiudad());
            evento.setUbicacion(eventoDetails.getUbicacion());
            evento.setDireccion(eventoDetails.getDireccion());
            evento.setAforo(eventoDetails.getAforo());
            evento.setFoto(eventoDetails.getFoto());
            evento.setCategoria(eventoDetails.getCategoria());
            return ResponseEntity.ok(eventoRepository.save(evento));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        if (eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}