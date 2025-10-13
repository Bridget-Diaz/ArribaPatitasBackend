package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.superpet.ProyectoSuperpet.model.ProductoDTO;
import java.util.List;
import java.util.stream.Collectors;

import com.superpet.ProyectoSuperpet.model.Producto;
import com.superpet.ProyectoSuperpet.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = {"*", "http://localhost:4200"}) 
public class ProductoController {

	
//no me fio de private final
	
    @Autowired
    private ProductoService productoService;

    @GetMapping("/listar")
    public List<ProductoDTO> listar() {
        return productoService.listarProductos()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    
    
    @GetMapping("/{id}")
    public ProductoDTO obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        return convertirADTO(producto);
    }

    
    
    @PostMapping
    public ProductoDTO guardar(@RequestBody ProductoDTO productoDTO) {
        Producto producto = convertirAEntidad(productoDTO);
        return convertirADTO(productoService.guardarProducto(producto));
    }

    
    
    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        Producto producto = convertirAEntidad(productoDTO);
        producto.setId(id);
        return convertirADTO(productoService.guardarProducto(producto));
    }

    //no se considero :v
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
    
    //aqui lo de dto
    //OE COMO Q NO TENEMOS TABLA CATEGORIA XD
    private ProductoDTO convertirADTO(Producto producto) {
        return new ProductoDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            //fue p sin categoria p no meto un string(ACTUALIZACION :ya pe huevon con un ENUM se lograba crj)
            producto.getPrecio(),
            producto.getStock()
        );
    }

    //de dto a entidad en 3 pasos 
    private Producto convertirAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        return producto;
    }
    
    
    
}
