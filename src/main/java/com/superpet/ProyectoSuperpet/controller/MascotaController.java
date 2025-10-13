package com.superpet.ProyectoSuperpet.controller;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import com.superpet.ProyectoSuperpet.model.Mascota;
import com.superpet.ProyectoSuperpet.model.MascotaDTO;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.service.MascotaService;
import com.superpet.ProyectoSuperpet.service.UsuarioService;

@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = {"*", "http://localhost:4200"}) 
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;
    
    
    //se agrego esta linea de usuario
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<MascotaDTO> listar() {
        List<Mascota> mascotas = mascotaService.listarMascotas();
        List<MascotaDTO> resultado = new ArrayList<>();
        for (Mascota m : mascotas) {
            resultado.add(new MascotaDTO(m));
        }
        return resultado;
    }
    
    
    @GetMapping("/{id}")
    public Mascota obtenerPorId(@PathVariable Long id) {
        return mascotaService.obtenerMascotaPorId(id);
    }

    @PostMapping
    public Mascota guardar(@RequestBody Mascota mascota) {
        return mascotaService.guardarMascota(mascota);
    }

    @PutMapping("/{id}")
    public Mascota actualizar(@PathVariable Long id, @RequestBody Mascota mascota) {
        mascota.setId(id);
        return mascotaService.guardarMascota(mascota);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
    }
    
//    //aca esto se agrego
//    
//    @PostMapping("/guardar")
//    public String guardarMascota(@ModelAttribute Mascota mascota ,Authentication auth ) {
//    	String email = auth.getName();
//    	Usuario usuario = usuarioService.buscarPorEmail(email);
//    //vincular mascota con el cliente de la cuenta
//    	mascota.setCliente(usuario.getCliente());
//    	mascotaService.guardarMascota(mascota);
//    	return"redirect:/miperfil";
//    }
    
}
