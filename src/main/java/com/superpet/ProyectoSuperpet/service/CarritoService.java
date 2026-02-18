package com.superpet.ProyectoSuperpet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.superpet.ProyectoSuperpet.model.ItemCarrito;

@Service
@SessionScope
/*

define un bean que se crea una sola vez por cada sesión HTTP
 de usuario. Este bean persiste durante toda la vida de dicha sesión 
 y se comparte entre múltiples peticiones HTTP del mismo usuario, siendo 
 ideal para almacenar datos de estado, como el carrito de compras o preferencias. 

*/

public class CarritoService {
	
	private List<ItemCarrito> items = new ArrayList<>();
	
	public void agregar(ItemCarrito item) {
		for(ItemCarrito i : items) {
			if(i.getIdProducto().equals(item.getIdProducto())) {
				i.setCantidad(i.getCantidad()+ item.getCantidad());
				return;
			}
		}
		items.add(item);
	}
	
	
	public void quitar(Long idProducto) {
		items.removeIf(i -> i.getIdProducto().equals(idProducto));
	}
	
	public List<ItemCarrito> listar(){
		return items;
	}
	
	
	public BigDecimal total() {
	    return items.stream()
	            .map(ItemCarrito::getSubtotal)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public void limpiar() {
		items.clear();
	}

}
