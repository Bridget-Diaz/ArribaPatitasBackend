package com.superpet.ProyectoSuperpet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.superpet.ProyectoSuperpet.model.Cita;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.service.CitaService;
import com.superpet.ProyectoSuperpet.service.UsuarioService;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CitaService citaService;

    // Ruta para la página principal
    @GetMapping("/")
    public String home(Model model) {
        return "home"; // Esto mostrará tu vista home.html
    }
    
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }
    
    @GetMapping("/menu")
    public String menu(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email);

            // Rol del usuario
            String rol = auth.getAuthorities().stream()
                    .map(Object::toString)
                    .findFirst()
                    .orElse("ROLE_CLIENTE");

            // Citas del cliente
            List<Cita> citasCliente = citaService.obtenerCitasPorCliente(usuario.getCliente());

            model.addAttribute("rol", rol);
            model.addAttribute("citas", citasCliente);
        }
        return "menu";
    }
}