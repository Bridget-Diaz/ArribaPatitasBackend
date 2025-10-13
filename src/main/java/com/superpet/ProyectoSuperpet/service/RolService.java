package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Rol;
import com.superpet.ProyectoSuperpet.repository.RolRepository;

@Service
public class RolService {

	@Autowired
	private RolRepository rolRepository;
	
	public List<Rol> listarRoles() {
		return rolRepository.findAll();
	}
	
	public Rol guardarRol(Rol rol) {
		return rolRepository.save(rol);
	}
	
	public Rol obtenerRolPorId(Long id) {
		return rolRepository.findById(id).orElse(null);
	}
	
	public void eliminarRol(Long id) {
		rolRepository.deleteById(id);
	}
}
