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
	
	 //aca esto se agrego
  
  @PostMapping("/guardar")
  public String guardarMascota(@ModelAttribute Mascota mascota ,Authentication auth ) {
  	String email = auth.getName();
  	Usuario usuario = usuarService.buscarPorEmail(email);
  //vincular mascota con el cliente de la cuenta
  	mascota.setCliente(usuario.getCliente());
  	mascoServiec.guardarMascota(mascota);
  	return"redirect:/miperfil";
  }
  
  
  
  
  @GetMapping("/nueva")
  public String mostrarFormularioMascota(Model model) {
      model.addAttribute("mascota", new Mascota());
      return "form_mascota"; 
  }

	
}
