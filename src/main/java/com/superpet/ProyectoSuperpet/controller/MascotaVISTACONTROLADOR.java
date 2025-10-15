package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superpet.ProyectoSuperpet.model.Mascota;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.service.MascotaService;
import com.superpet.ProyectoSuperpet.service.UsuarioService;

@Controller
@RequestMapping("/mascotas")
public class MascotaVISTACONTROLADOR {

	@Autowired
	private MascotaService mascoServiec;
	
	@Autowired
	private UsuarioService usuarService;
	
  
	@PostMapping("/guardar")
	public String guardarMascota(@ModelAttribute Mascota mascota, 
	                             Authentication auth, 
	                             Model model) {
	    String email = auth.getName();
	    Usuario usuario = usuarService.buscarPorEmail(email);
	    mascota.setCliente(usuario.getCliente());
	    mascoServiec.guardarMascota(mascota);
	    model.addAttribute("mensajeExito", "Mascota registrada correctamente 🐶");
	    model.addAttribute("mascota", new Mascota());
	    return "form_mascota"; 
	}

  
  @GetMapping("/nueva")
  public String mostrarFormularioMascota(Model model) {
      model.addAttribute("mascota", new Mascota());
      return "form_mascota"; 
  }

	
}
