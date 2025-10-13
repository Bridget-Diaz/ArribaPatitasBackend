package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Veterinario;
import com.superpet.ProyectoSuperpet.repository.VeterinarioRepository;

@Service
public class VeterinarioService {

	@Autowired
	private VeterinarioRepository veterinarioRepository;
	
	public List<Veterinario> listarVeterinarios() {
		return veterinarioRepository.findAll();
	}
	
	public Veterinario guardarVeterinario(Veterinario veterinario) {
		return veterinarioRepository.save(veterinario);
	}
	
	public Veterinario obtenerVeterinarioPorId(Long id) {
		return veterinarioRepository.findById(id).orElse(null);
	}
	
	public void eliminarVeterinario(Long id) {
		veterinarioRepository.deleteById(id);
	}
}
