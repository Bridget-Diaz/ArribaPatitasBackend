package com.superpet.ProyectoSuperpet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.superpet.ProyectoSuperpet.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
	Optional<Rol> findByNombre(String nombre);
}
