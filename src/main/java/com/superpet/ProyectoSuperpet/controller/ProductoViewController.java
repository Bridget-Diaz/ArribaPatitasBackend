package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.superpet.ProyectoSuperpet.service.ProductoService;

@Controller
public class ProductoViewController {

    @Autowired
    private ProductoService productoService;

    // LISTA DE PRODUCTOS
    @GetMapping("/productos")
    public String verProductos(Model model) {
        model.addAttribute("productos", productoService.listarProductos());
        return "productos"; // templates/productos.html
    }

    // DETALLE DE PRODUCTO
    @GetMapping("/productos/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.obtenerProductoPorId(id));
        return "producto-detalle"; // templates/producto-detalle.html
    }
}
