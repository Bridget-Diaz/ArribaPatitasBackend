package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public List<Usuario> listarUsuarios() {
		return usuarioRepository.findAll();
	}
	
	public Usuario guardarUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	public Usuario obtenerUsuarioPorId(Long id) {
		return usuarioRepository.findById(id).orElse(null);
	}
	
	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}
	
	public Usuario buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
}
