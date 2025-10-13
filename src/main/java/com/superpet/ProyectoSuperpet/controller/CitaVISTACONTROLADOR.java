package com.superpet.ProyectoSuperpet.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superpet.ProyectoSuperpet.model.Cita;
import com.superpet.ProyectoSuperpet.model.Mascota;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.model.Veterinario;
import com.superpet.ProyectoSuperpet.service.CitaService;
import com.superpet.ProyectoSuperpet.service.MascotaService;
import com.superpet.ProyectoSuperpet.service.ServicioService;
import com.superpet.ProyectoSuperpet.service.UsuarioService;
import com.superpet.ProyectoSuperpet.service.VeterinarioService;

@Controller
@RequestMapping("/citas")
public class CitaVISTACONTROLADOR {

	 @Autowired
	    private CitaService citaService;
	    @Autowired
	    private MascotaService mascotaService;
	    @Autowired
	    private ServicioService servicioService;
	    @Autowired
	    private UsuarioService usuarioService;

	    @Autowired
	    private VeterinarioService veterinarioService;
	    @GetMapping("/nueva")
	    public String mostrarFormulario(Model model, Principal principal) {
	        // usuario logeado p papi
	        String email = principal.getName();
	        Usuario usuario = usuarioService.buscarPorEmail(email);
	        Long clienteId = usuario.getCliente().getId();

	        // Mascotas
	        List<Mascota> mascotas = mascotaService.listarPorCliente(clienteId);
	        List<Veterinario> veterinarios = veterinarioService.listarVeterinarios();

	        //llamar a los servicios
	        model.addAttribute("servicios", servicioService.listarServicios());

	        // Generar horarios disponibles (ejemplo: próximos 7 días, de 9am a 5pm cada hora)
	        List<LocalDateTime> horariosDisponibles = new java.util.ArrayList<>();//aca llore para que funcione
	        LocalDate hoy = LocalDate.now();
	        for (int d = 0; d < 7; d++) { //7 días
	            for (int h = 9; h <= 17; h++) { //de 9 a 17 hs osea 5 hrs
	                horariosDisponibles.add(hoy.plusDays(d).atTime(h, 0));
	            }
	        }

	        // Pasar datos al modelo
	        model.addAttribute("cita", new Cita());
	        model.addAttribute("mascotas", mascotas);
	        model.addAttribute("veterinarios",veterinarios);
	        model.addAttribute("horariosDisponibles", horariosDisponibles);

	        return "form_cita"; // busca templates/form_cita.html
	    }



	    @PostMapping("/guardar")
	    public String guardarCita(@ModelAttribute Cita cita, Authentication auth) {
	        String email = auth.getName();
	        Usuario usuario = usuarioService.buscarPorEmail(email);

	        cita.setCliente(usuario.getCliente());
	        cita.setEstado("Pendiente");
	        citaService.guardarCita(cita);

	        return "redirect:/menu";
	    }
}
