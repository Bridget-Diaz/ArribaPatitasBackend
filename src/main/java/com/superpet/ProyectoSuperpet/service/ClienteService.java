package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Cliente;
import com.superpet.ProyectoSuperpet.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public List<Cliente> listarClientes() {
		return clienteRepository.findAll();
	}
	
	public Cliente guardarCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	public Cliente obtenerClientePorId(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}
	
	public void eliminarCliente(Long id) {
		clienteRepository.deleteById(id);
	}
}
