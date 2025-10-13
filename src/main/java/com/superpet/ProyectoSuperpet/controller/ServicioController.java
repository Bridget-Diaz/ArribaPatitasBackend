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
import com.superpet.ProyectoSuperpet.model.Servicio;
import com.superpet.ProyectoSuperpet.service.ServicioService;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = {"*", "http://localhost:4200"}) 
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/listar")
    public List<Servicio> listar() {
        return servicioService.listarServicios();
    }

    @GetMapping("/{id}")
    public Servicio obtenerPorId(@PathVariable Long id) {
        return servicioService.obtenerServicioPorId(id);
    }

    @PostMapping
    public Servicio guardar(@RequestBody Servicio servicio) {
        return servicioService.guardarServicio(servicio);
    }

    @PutMapping("/{id}")
    public Servicio actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        servicio.setId(id);
        return servicioService.guardarServicio(servicio);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
    }
}
