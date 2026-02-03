package com.tickets4u.events.repositories;

import com.tickets4u.events.models.Estadisticas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadisticasRepository extends JpaRepository<Estadisticas, Long> {
}