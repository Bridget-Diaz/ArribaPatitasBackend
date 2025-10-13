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
import com.superpet.ProyectoSuperpet.model.Rol;
import com.superpet.ProyectoSuperpet.service.RolService;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = {"*", "http://localhost:4200"}) 
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping("/listar")
    public List<Rol> listar() {
        return rolService.listarRoles();
    }

    @GetMapping("/{id}")
    public Rol obtenerPorId(@PathVariable Long id) {
        return rolService.obtenerRolPorId(id);
    }

    @PostMapping
    public Rol guardar(@RequestBody Rol rol) {
        return rolService.guardarRol(rol);
    }

    @PutMapping("/{id}")
    public Rol actualizar(@PathVariable Long id, @RequestBody Rol rol) {
        rol.setId(id);
        return rolService.guardarRol(rol);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        rolService.eliminarRol(id);
    }
}
