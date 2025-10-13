package com.superpet.ProyectoSuperpet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    // Puede ser null para admins no asociados a cliente
    @ManyToOne
    @JoinColumn(name = "id_cliente", foreignKey = @ForeignKey(name = "Usuarios_Clientes_fk"))
    private Cliente cliente;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rol", nullable = false, foreignKey = @ForeignKey(name = "Usuarios_Roles_fk"))
    private Rol rol;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	
	
	
    // Getters y setters...
}