package com.superpet.ProyectoSuperpet.model;

import java.math.BigDecimal;

public class ItemCarrito {

	    private Long idProducto;
	    private String nombre;
	    private BigDecimal precio;
	    private int cantidad;

	    
	    public BigDecimal getSubtotal() {
	        return precio.multiply(BigDecimal.valueOf(cantidad));
	    }
	    
	    public ItemCarrito() {}

	    
	    
	    
		public ItemCarrito(Long idProducto, String nombre, BigDecimal precio, int cantidad) {
			super();
			this.idProducto = idProducto;
			this.nombre = nombre;
			this.precio = precio;
			this.cantidad = cantidad;
		}

		public Long getIdProducto() {
			return idProducto;
		}

		public void setIdProducto(Long idProducto) {
			this.idProducto = idProducto;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public BigDecimal getPrecio() {
			return precio;
		}

		public void setPrecio(BigDecimal precio) {
			this.precio = precio;
		}

		public int getCantidad() {
			return cantidad;
		}

		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}

	   
	    
}
