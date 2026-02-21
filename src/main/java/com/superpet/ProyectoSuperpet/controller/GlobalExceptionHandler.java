package com.superpet.ProyectoSuperpet.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
		
	@ExceptionHandler(RuntimeException.class)
    public String manejarRuntime(RuntimeException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error"; // error.html
    }

    @ExceptionHandler(Exception.class)
    public String manejarGeneral(Exception ex, Model model) {

        model.addAttribute("mensaje", "Ocurrió un error inesperado");

        return "error";
    }
}
	
	
