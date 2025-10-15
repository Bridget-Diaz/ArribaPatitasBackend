package com.superpet.ProyectoSuperpet.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.superpet.ProyectoSuperpet.model.Cita;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.service.CitaService;
import com.superpet.ProyectoSuperpet.service.UsuarioService;
import com.superpet.ProyectoSuperpet.repository.UsuarioRepository;



@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CitaService citaService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Página principal
    @GetMapping("/")
    public String home() {
        return "home"; // templates/home.html
    }

    // Página de login
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    // Página del menú (listado de citas)
    @GetMapping("/menu")
    public String mostrarMenu(Model model, Principal principal) {
        if (principal != null) {
            String correo = principal.getName();
            Usuario usuario = usuarioRepository.findByEmail(correo);

            if (usuario != null && usuario.getCliente() != null) {
                List<Cita> citasCliente = citaService.obtenerCitasPorCliente(usuario.getCliente());
                model.addAttribute("citas", citasCliente);
            }
        }

        return "menu";
    }
    
 // Página para ver las citas del usuario autenticado
    @GetMapping("/mis-citas")
    public String verMisCitas(Model model, Principal principal) {
        if (principal != null) {
            String correo = principal.getName();
            Usuario usuario = usuarioRepository.findByEmail(correo);

            if (usuario != null && usuario.getCliente() != null) {
                List<Cita> citasCliente = citaService.obtenerCitasPorCliente(usuario.getCliente());
                model.addAttribute("citas", citasCliente);
                model.addAttribute("usuario", usuario);
            }
        }
        return "mis_citas"; // templates/mis_citas.html
    }



}
