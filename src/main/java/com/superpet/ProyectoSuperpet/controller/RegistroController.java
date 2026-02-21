package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.superpet.ProyectoSuperpet.model.Cliente;
import com.superpet.ProyectoSuperpet.model.Rol;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.repository.ClienteRepository;
import com.superpet.ProyectoSuperpet.repository.RolRepository;
import com.superpet.ProyectoSuperpet.repository.UsuarioRepository;
import com.superpet.ProyectoSuperpet.service.UsuarioService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormulario() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam("nombre") String nombre,
                                   @RequestParam("telefono") String telefono,
                                   @RequestParam("email") String email,
                                   @RequestParam("direccion") String direccion,
                                   @RequestParam("password") String password,
                                   RedirectAttributes redirectAttributes) {
        try {
       
            if (usuarioService.buscarPorEmail(email) != null) {
                redirectAttributes.addFlashAttribute("error", "Ya existe una cuenta con este correo.");
                return "redirect:/";
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);
            cliente.setDireccion(direccion);
            clienteRepo.save(cliente);

            Rol rolCliente = rolRepo.findByNombre("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRol(rolCliente);
            usuario.setCliente(cliente);
            usuarioRepo.save(usuario);

            redirectAttributes.addFlashAttribute("registroExitoso", true);

            return "redirect:/login";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            System.out.println("REGISTRANDO USUARIO: " + email);
            return "redirect:/";
        }
    }
}