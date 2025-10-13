package com.superpet.ProyectoSuperpet.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.superpet.ProyectoSuperpet.model.Cita;
import com.superpet.ProyectoSuperpet.model.Cliente;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

	//amen aunque no tiene sentido ya :(
	  @Query("SELECT COUNT(c) FROM Cita c WHERE c.fechaCita >= :inicio AND c.fechaCita < :fin")
	    int contarCitasEnFranja(@Param("inicio") LocalDateTime inicio,
	                            @Param("fin") LocalDateTime fin);
	  
	  List<Cita> findByCliente(Cliente cliente);

	  
	  

}
