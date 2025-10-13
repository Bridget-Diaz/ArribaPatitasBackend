package com.superpet.ProyectoSuperpet.controller;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.service.UsuarioService;

@Controller
public class PerfilController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/miperfil")
	public String verPerfil(Model model,Authentication auth) {
		String emailuser = auth.getName();
		
		Usuario usuario= usuarioService.buscarPorEmail(emailuser);
		model.addAttribute("usuario",usuario);
		return "miperfil";
	}
}
