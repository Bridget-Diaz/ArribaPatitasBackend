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
import com.superpet.ProyectoSuperpet.model.Cita;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.service.CitaService;
import com.superpet.ProyectoSuperpet.service.MascotaService;
import com.superpet.ProyectoSuperpet.service.ServicioService;
import com.superpet.ProyectoSuperpet.service.UsuarioService;


@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = {"*", "http://localhost:4200"}) 
public class CitaController {

    @Autowired
    private CitaService citaService;
    
    
    //aqui agrego la mayoria de cosas
    @Autowired
    private MascotaService mascotaService;
    
    @Autowired 
    private ServicioService servicioService;
    
    @Autowired UsuarioService usuarioService;

    
    
    @GetMapping("/listar")
    public List<Cita> listar() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public Cita obtenerPorId(@PathVariable Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    @PostMapping
    public Cita guardar(@RequestBody Cita cita) {
        return citaService.guardarCita(cita);
    }

    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        cita.setId(id);
        return citaService.guardarCita(cita);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        citaService.eliminarCita(id);
    }
    
    //por aca 
    
//    @GetMapping("/nueva")
//    public String mostrarFormulario(Model model, Authentication auth) {
//        String email = auth.getName();
//        Usuario usuario = usuarioService.buscarPorEmail(email);
//
//        model.addAttribute("cita", new Cita());
//        model.addAttribute("mascotas", mascotaService.listarPorCliente(usuario.getCliente().getId()));
//        model.addAttribute("servicios", servicioService.listarServicios());
//
//        return "form_cita";
//    }
//
//
//    @PostMapping("/guardar")
//    public String guardarCita(@ModelAttribute Cita cita, Authentication auth) {
//        String email = auth.getName();
//        Usuario usuario = usuarioService.buscarPorEmail(email);
//
//        cita.setCliente(usuario.getCliente());
//        cita.setEstado("Pendiente");
//        citaService.guardarCita(cita);
//
//        return "redirect:/miperfil";
//    }
//    
    
    
}