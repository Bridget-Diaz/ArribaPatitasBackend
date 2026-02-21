package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.superpet.ProyectoSuperpet.model.ProductoDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
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
            producto.getCategoria(),   
            producto.getPrecio(),
            producto.getStock(),
            producto.getImagen()
        );
    }


    //de dto a entidad en 3 pasos 
    private Producto convertirAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagen(dto.getImagen());
        return producto;
    }
    
    
    
    
    @PostMapping("/guardar-con-imagen")
    public ResponseEntity<ProductoDTO> guardarConImagen(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoria") String categoria,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("imagen") MultipartFile imagenFile
    ) throws IOException {

    	String rutaBase = "C:/Users/Josue/Downloads/Grupo13 proyecto/uploads/productos/";
        File carpeta = new File(rutaBase);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Nombre único para que no se repita
        String nombreArchivo = UUID.randomUUID() + "_" + imagenFile.getOriginalFilename();
        Path rutaCompleta = Paths.get(rutaBase + nombreArchivo);

        Files.write(rutaCompleta, imagenFile.getBytes());

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCategoria(categoria);
        producto.setPrecio(new java.math.BigDecimal(precio));
        producto.setStock(stock);

        // esta ruta es la que se guardará en BD
        producto.setImagen("/uploads/productos/" + nombreArchivo);

        Producto guardado = productoService.guardarProducto(producto);

        return ResponseEntity.ok(convertirADTO(guardado));
    }

    
    @PutMapping("/actualizar-con-imagen/{id}")
    public ResponseEntity<ProductoDTO> actualizarConImagen(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("categoria") String categoria,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "imagen", required = false) MultipartFile imagenFile
    ) throws IOException {

        Producto producto = productoService.obtenerProductoPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCategoria(categoria);
        producto.setPrecio(new java.math.BigDecimal(precio));
        producto.setStock(stock);

        if (imagenFile != null && !imagenFile.isEmpty()) {
        	String rutaBase = System.getProperty("user.dir") + "/uploads/productos/";
            File carpeta = new File(rutaBase);

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String nombreArchivo = UUID.randomUUID() + "_" + imagenFile.getOriginalFilename();
            Path rutaCompleta = Paths.get(rutaBase + nombreArchivo);

            Files.write(rutaCompleta, imagenFile.getBytes());

            producto.setImagen("/uploads/productos/" + nombreArchivo);
        }

        Producto actualizado = productoService.guardarProducto(producto);

        return ResponseEntity.ok(convertirADTO(actualizado));
    }
 
    
    
    
    
}
