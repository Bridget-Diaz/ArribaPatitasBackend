package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Pedido;
import com.superpet.ProyectoSuperpet.repository.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	public List<Pedido> listarPedidos() {
		return pedidoRepository.findAll();
	}
	
	public Pedido guardarPedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}
	
	public Pedido obtenerPedidoPorId(Long id) {
		return pedidoRepository.findById(id).orElse(null);
	}
	
	public void eliminarPedido(Long id) {
		pedidoRepository.deleteById(id);
	}
}
