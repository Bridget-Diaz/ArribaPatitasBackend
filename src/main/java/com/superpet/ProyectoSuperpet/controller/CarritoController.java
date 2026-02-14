package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.superpet.ProyectoSuperpet.model.ItemCarrito;
import com.superpet.ProyectoSuperpet.model.Producto;
import com.superpet.ProyectoSuperpet.service.CarritoService;
import com.superpet.ProyectoSuperpet.service.ProductoService;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
	
		private final CarritoService carritoService;
	    private final ProductoService productoService;

	    public CarritoController(CarritoService carritoService,
	                             ProductoService productoService) {
	        this.carritoService = carritoService;
	        this.productoService = productoService;
	    }

	    @GetMapping
	    public String verCarrito(Model model) {
	        model.addAttribute("items", carritoService.listar());
	        model.addAttribute("total", carritoService.total());
	        return "carrito";
	    }

	    @PostMapping("/agregar/{id}")
	    public String agregar(@PathVariable Long id,
	                          @RequestParam int cantidad) {

	        Producto p = productoService.obtenerProductoPorId(id);

	        ItemCarrito item = new ItemCarrito();
	        item.setIdProducto(p.getId());
	        item.setNombre(p.getNombre());
	        item.setPrecio(p.getPrecio());
	        item.setCantidad(cantidad);

	        carritoService.agregar(item);

	        return "redirect:/carrito";
	    }

	    @GetMapping("/quitar/{id}")
	    public String quitar(@PathVariable Long id) {
	        carritoService.quitar(id);
	        return "redirect:/carrito";
	    }
	    
	    @PostMapping("/limpiar")
	    public String limpiar() {
	        carritoService.limpiar();
	        return "redirect:/carrito";
	    }
	}