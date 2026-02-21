package com.superpet.ProyectoSuperpet.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.superpet.ProyectoSuperpet.model.EstadoPedido;
import com.superpet.ProyectoSuperpet.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	List<Pedido> findByCliente_Id(Long idCliente);
	List<Pedido> findByEstadoAndFechaLimitePagoBefore(
		    EstadoPedido estado,
		    LocalDateTime fecha
		);

}
