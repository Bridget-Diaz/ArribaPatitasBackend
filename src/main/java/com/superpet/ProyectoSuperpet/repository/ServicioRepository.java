package com.superpet.ProyectoSuperpet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.superpet.ProyectoSuperpet.model.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
