package com.superpet.ProyectoSuperpet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Cita;
import com.superpet.ProyectoSuperpet.model.Cliente;
import com.superpet.ProyectoSuperpet.repository.CitaRepository;

@Service
public class CitaService {

	@Autowired
	private CitaRepository citaRepository;
	
	public List<Cita> listarCitas() {
		return citaRepository.findAll();
	}
	
	public Cita guardarCita(Cita cita) {
		return citaRepository.save(cita);
	}
	
	public Cita obtenerCitaPorId(Long id) {
		return citaRepository.findById(id).orElse(null);
	}
	
	public void eliminarCita(Long id) {
		citaRepository.deleteById(id);
	}
	
	public List<LocalDateTime> generarHorariosDisponibles(LocalDate fecha) {
        List<LocalDateTime> disponibles = new ArrayList<>();
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(17, 0);

        while (!inicio.isAfter(fin.minusMinutes(30))) {
            LocalDateTime slotInicio = LocalDateTime.of(fecha, inicio);
            LocalDateTime slotFin = slotInicio.plusMinutes(30);

            int count = citaRepository.contarCitasEnFranja(slotInicio, slotFin);

            if (count < 2) { // máximo 2 citas por franja
            	//alguien probo esto? xddddddd 
                disponibles.add(slotInicio);
            } 	

            inicio = inicio.plusMinutes(30);
        }
        return disponibles;
    }
	
	//esto se agrego
	public List<Cita> obtenerCitasPorCliente(Cliente cliente) {
	    return citaRepository.findByCliente(cliente);
	}

}
