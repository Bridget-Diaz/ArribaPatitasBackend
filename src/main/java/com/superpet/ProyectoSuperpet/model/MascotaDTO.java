package com.superpet.ProyectoSuperpet.model;

public class MascotaDTO {
    public Long id;
    public String propietario;
    public String nombre;
    public String especie;
    public String raza;
    public Integer edad;
    public Double peso;
    public String observaciones;

    public MascotaDTO(Mascota m) {
        this.id = m.getId();
        this.propietario = m.getCliente().getNombre();
        this.nombre = m.getNombre();
        this.especie = m.getEspecie();
        this.raza = m.getRaza();
        this.edad = m.getEdad();
        this.peso = m.getPeso();
        this.observaciones = m.getObservaciones();
    }
}