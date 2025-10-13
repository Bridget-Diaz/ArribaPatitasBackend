package com.superpet.ProyectoSuperpet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "Mascotas")
public class Mascota {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getRaza() {
		return raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente", nullable = false, foreignKey = @ForeignKey(name = "Mascotas_Clientes_fk"))

    private Cliente cliente;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 50)
    private String especie;

    @Column(length = 50)
    private String raza;

    private Integer edad;         // años aproximados
    private Double peso;          // kg

    @Lob
    private String observaciones;

    // Getters y setters...
}
