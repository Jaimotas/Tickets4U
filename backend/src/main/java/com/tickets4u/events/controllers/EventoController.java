package com.tickets4u.events.controllers;

import com.tickets4u.events.models.Evento;
import com.tickets4u.events.models.EventoDto;
import com.tickets4u.events.repositories.EventoRepository;
import com.tickets4u.events.services.EventoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
public class EventoController {

    private final EventoRepository eventoRepository;
    private final EventoService eventoService;

    public EventoController(EventoRepository eventoRepository,
                            EventoService eventoService) {
        this.eventoRepository = eventoRepository;
        this.eventoService = eventoService;
    }

    // ----- ENDPOINTS QUE USA EL FRONT (CON DTO) -----

    @GetMapping
    public List<EventoDto> getAllEventos() {
        return eventoService.listarEventos();
    }

    @GetMapping("/{id}")
    public EventoDto getEventoById(@PathVariable Long id) {
        return eventoService.obtenerEventoPorId(id);
    }

    // ----- CRUD INTERNO (SIGUE TRABAJANDO CON ENTIDAD) -----

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
