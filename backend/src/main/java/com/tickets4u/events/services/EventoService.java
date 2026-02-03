package com.tickets4u.events.services;

import com.tickets4u.events.models.Estadisticas;
import com.tickets4u.events.models.Evento;
import com.tickets4u.events.models.EventoDto;
import com.tickets4u.events.repositories.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    private EventoDto toDto(Evento evento) {
        EventoDto dto = new EventoDto();
        dto.setId(evento.getId());
        dto.setNombre(evento.getNombre());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaInicio(evento.getFechaInicio());
        dto.setFechaFin(evento.getFechaFin());
        dto.setCiudad(evento.getCiudad());
        dto.setUbicacion(evento.getUbicacion());
        dto.setDireccion(evento.getDireccion());
        dto.setAforo(evento.getAforo());
        dto.setFoto(evento.getFoto());
        dto.setCategoria(evento.getCategoria().name());

        Estadisticas est = evento.getEstadisticas();
        if (est != null) {
            dto.setTicketsDisponibles(est.getTicketsDisponibles());
            dto.setTicketsVendidos(est.getTicketsVendidos());
            dto.setIngresos(est.getIngresos());
        }

        return dto;
    }

    public List<EventoDto> listarEventos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EventoDto obtenerEventoPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        return toDto(evento);
    }
}
