package com.tickets4u.events.controllers;

import com.tickets4u.events.repositories.EventoRepository;
import com.tickets4u.models.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public Optional<Evento> getEventoById(@PathVariable Long id) {
        return eventoRepository.findById(id);
    }
    
    @PostMapping
    public Evento createEvento(@RequestBody Evento evento) {
        return eventoRepository.save(evento);
    }
    
    @PutMapping("/{id}")
    public Evento updateEvento(@PathVariable Long id, @RequestBody Evento eventoDetails) {
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
            return eventoRepository.save(evento);
        }).orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }
    
    @DeleteMapping("/{id}")
    public void deleteEvento(@PathVariable Long id) {
        eventoRepository.deleteById(id);
    }
}