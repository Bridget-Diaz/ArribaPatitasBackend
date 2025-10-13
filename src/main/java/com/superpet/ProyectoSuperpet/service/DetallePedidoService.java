package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.DetallePedido;
import com.superpet.ProyectoSuperpet.repository.DetallePedidoRepository;

@Service
public class DetallePedidoService {

	@Autowired
	private DetallePedidoRepository detallePedidoRepository;
	
	public List<DetallePedido> listarDetallesPedido() {
		return detallePedidoRepository.findAll();
	}
	
	public DetallePedido guardarDetallePedido(DetallePedido detallePedido) {
		return detallePedidoRepository.save(detallePedido);
	}
	
	public DetallePedido obtenerDetallePedidoPorId(Long id) {
		return detallePedidoRepository.findById(id).orElse(null);
	}
	
	public void eliminarDetallePedido(Long id) {
		detallePedidoRepository.deleteById(id);
	}
}
