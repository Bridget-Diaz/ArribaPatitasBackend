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
import com.superpet.ProyectoSuperpet.model.Pedido;
import com.superpet.ProyectoSuperpet.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = {"*", "http://localhost:4200"}) 
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/listar")
    public List<Pedido> listar() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    public Pedido obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPedidoPorId(id);
    }

    @PostMapping
    public Pedido guardar(@RequestBody Pedido pedido) {
        return pedidoService.guardarPedido(pedido);
    }

    @PutMapping("/{id}")
    public Pedido actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        pedido.setId(id);
        return pedidoService.guardarPedido(pedido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
    }
}
