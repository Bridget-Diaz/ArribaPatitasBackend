package com.superpet.ProyectoSuperpet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.superpet.ProyectoSuperpet.model.Veterinario;
import com.superpet.ProyectoSuperpet.service.VeterinarioService;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    @GetMapping("/listar")
    public List<Veterinario> listar() {
        return veterinarioService.listarVeterinarios();
    }

    @GetMapping("/{id}")
    public Veterinario obtenerPorId(@PathVariable Long id) {
        return veterinarioService.obtenerVeterinarioPorId(id);
    }

    @PostMapping
    public Veterinario guardar(@RequestBody Veterinario veterinario) {
        return veterinarioService.guardarVeterinario(veterinario);
    }

    @PutMapping("/{id}")
    public Veterinario actualizar(@PathVariable Long id, @RequestBody Veterinario veterinario) {
        veterinario.setId(id);
        return veterinarioService.guardarVeterinario(veterinario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
    }
}
