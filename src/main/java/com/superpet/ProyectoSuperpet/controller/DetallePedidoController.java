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

import java.util.List;
import com.superpet.ProyectoSuperpet.model.DetallePedido;
import com.superpet.ProyectoSuperpet.service.DetallePedidoService;

@RestController
@RequestMapping("/api/detallepedidos")
@CrossOrigin(origins = {"*", "http://localhost:4200"}) 
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping("/listar")
    public List<DetallePedido> listar() {
        return detallePedidoService.listarDetallesPedido();
    }

    @GetMapping("/{id}")
    public DetallePedido obtenerPorId(@PathVariable Long id) {
        return detallePedidoService.obtenerDetallePedidoPorId(id);
    }

    @PostMapping
    public DetallePedido guardar(@RequestBody DetallePedido detallePedido) {
        return detallePedidoService.guardarDetallePedido(detallePedido);
    }

    @PutMapping("/{id}")
    public DetallePedido actualizar(@PathVariable Long id, @RequestBody DetallePedido detallePedido) {
        detallePedido.setId(id);
        return detallePedidoService.guardarDetallePedido(detallePedido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        detallePedidoService.eliminarDetallePedido(id);
    }
}
