package com.superpet.ProyectoSuperpet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.superpet.ProyectoSuperpet.model.Cliente;
import com.superpet.ProyectoSuperpet.model.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
	List<Mascota> findByClienteId(Long clienteId);
	
	List<Mascota> findByCliente(Cliente cliente);
	
}
