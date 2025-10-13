package com.superpet.ProyectoSuperpet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Producto;
import com.superpet.ProyectoSuperpet.repository.ProductoRepository;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository productoRepository;
	
	public List<Producto> listarProductos() {
		return productoRepository.findAll();
	}
	
	public Producto guardarProducto(Producto producto) {
		return productoRepository.save(producto);
	}
	
	public Producto obtenerProductoPorId(Long id) {
		return productoRepository.findById(id).orElse(null);
	}
	
	public void eliminarProducto(Long id) {
		productoRepository.deleteById(id);
	}
}
