package com.superpet.ProyectoSuperpet.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superpet.ProyectoSuperpet.model.Cliente;
import com.superpet.ProyectoSuperpet.service.CarritoService;
import com.superpet.ProyectoSuperpet.service.ClienteService;
import com.superpet.ProyectoSuperpet.service.PedidoService;

@Controller
@RequestMapping("/pedido")
public class PedidoViewController {

    private final CarritoService carritoService;
    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    public PedidoViewController(CarritoService carritoService,
                                PedidoService pedidoService,
                                ClienteService clienteService) {
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    @PostMapping("/confirmar")
    public String confirmar(Principal principal) {
        String email = principal.getName();
        Cliente cliente = clienteService.buscarPorEmail(email);
        pedidoService.crearPedido(cliente, carritoService.listar());
        carritoService.limpiar();
        return "redirect:/pedido/mis-pedidos";
    }

    
    
    @GetMapping("/mis-pedidos")
    public String misPedidos(Model model, Principal principal) {

        String email = principal.getName(); // aquí SÍ funciona
        Cliente cliente = clienteService.buscarPorEmail(email);

        model.addAttribute("pedidos",
            pedidoService.listarPedidosPorCliente(cliente.getId())
        );

        return "mis-pedidos";
    }


    
}
