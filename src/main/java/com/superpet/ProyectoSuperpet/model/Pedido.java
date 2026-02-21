package com.superpet.ProyectoSuperpet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente", nullable = false, foreignKey = @ForeignKey(name = "Pedidos_Clientes_fk"))
    private Cliente cliente;

    @Column(name="fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoPedido estado;


    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

    private LocalDateTime fechaLimitePago;

    
 
    public LocalDateTime getFechaLimitePago() {
		return fechaLimitePago;
	}



	public void setFechaLimitePago(LocalDateTime fechaLimitePago) {
		this.fechaLimitePago = fechaLimitePago;
	}



	@PrePersist
    public void prePersist() {
        this.fechaPedido = LocalDateTime.now();
    }


    
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

	public LocalDateTime getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(LocalDateTime fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	

	public EstadoPedido getEstado() {
		return estado;
	}



	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
	}



	public List<DetallePedido> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetallePedido> detalles) {
		this.detalles = detalles;
	}

    // Getters y setters...
}