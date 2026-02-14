package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superpet.ProyectoSuperpet.dto.ChatRequest;
import com.superpet.ProyectoSuperpet.dto.ChatResponse;
import com.superpet.ProyectoSuperpet.service.ChatService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/chat")
public class ChatController {

	 @Autowired
	    private ChatService chatService;
	    
	 @PostMapping
	 public ChatResponse chatear(@RequestBody ChatRequest request, HttpSession session) {

	     boolean autenticado = SecurityContextHolder.getContext()
	             .getAuthentication()
	             .isAuthenticated();

	     return chatService.procesarMensaje(
	         request.getMensaje(),
	         autenticado,
	         session
	     );
	 }

}
