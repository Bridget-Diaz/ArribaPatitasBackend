package com.superpet.ProyectoSuperpet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.superpet.ProyectoSuperpet.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	List<Producto> findByNombreContainingIgnoreCase(String nombre);
	List<Producto> findTop5ByNombreContainingIgnoreCase(String nombre);


}
