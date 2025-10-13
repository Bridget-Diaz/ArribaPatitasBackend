package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Mascota;
import com.superpet.ProyectoSuperpet.repository.MascotaRepository;

@Service
public class MascotaService {

	@Autowired
	private MascotaRepository mascotaRepository;
	
	public List<Mascota> listarMascotas() {
		return mascotaRepository.findAll();
	}
	
	public Mascota guardarMascota(Mascota mascota) {
		return mascotaRepository.save(mascota);
	}
	
	public Mascota obtenerMascotaPorId(Long id) {
		return mascotaRepository.findById(id).orElse(null);
	}
	
	public void eliminarMascota(Long id) {
		mascotaRepository.deleteById(id);
	}
	
	public List<Mascota> listarPorCliente(Long clienteId){
		return mascotaRepository.findByClienteId(clienteId);
	}
	
}
