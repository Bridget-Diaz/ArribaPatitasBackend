package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Servicio;
import com.superpet.ProyectoSuperpet.repository.ServicioRepository;

@Service
public class ServicioService {

	@Autowired
	private ServicioRepository servicioRepository;
	
	public List<Servicio> listarServicios() {
		return servicioRepository.findAll();
	}
	
	public Servicio guardarServicio(Servicio servicio) {
		return servicioRepository.save(servicio);
	}
	
	public Servicio obtenerServicioPorId(Long id) {
		return servicioRepository.findById(id).orElse(null);
	}
	
	public void eliminarServicio(Long id) {
		servicioRepository.deleteById(id);
	}
}
