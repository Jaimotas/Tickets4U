package com.tickets4u.events.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickets4u.models.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
}