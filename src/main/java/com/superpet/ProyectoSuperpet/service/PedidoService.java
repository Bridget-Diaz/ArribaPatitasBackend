package com.superpet.ProyectoSuperpet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.model.Cliente;
import com.superpet.ProyectoSuperpet.model.DetallePedido;
import com.superpet.ProyectoSuperpet.model.ItemCarrito;
import com.superpet.ProyectoSuperpet.model.Pedido;
import com.superpet.ProyectoSuperpet.repository.InventarioRepository;
import com.superpet.ProyectoSuperpet.repository.PedidoRepository;
import com.superpet.ProyectoSuperpet.repository.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;
import com.superpet.ProyectoSuperpet.model.Inventario;
import com.superpet.ProyectoSuperpet.model.TipoMovimiento;
import com.superpet.ProyectoSuperpet.model.Producto;
import com.superpet.ProyectoSuperpet.model.EstadoPedido;



@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoRepository productoRepo;
    private final InventarioRepository inventarioRepo;
    

    public PedidoService(PedidoRepository pedidoRepo,
                         ProductoRepository productoRepo,
                         InventarioRepository inventarioRepo) {
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
        this.inventarioRepo = inventarioRepo;
    }


    public List<Pedido> listarPedidos() {
        return pedidoRepo.findAll();
    }

    public Pedido guardarPedido(Pedido pedido) {
        return pedidoRepo.save(pedido);
    }

    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepo.findById(id).orElse(null);
    }

    public void eliminarPedido(Long id) {
        pedidoRepo.deleteById(id);
    }

    // ⭐⭐⭐ ESTE ES EL QUE TE FALTABA
    public List<Pedido> listarPedidosPorCliente(Long idCliente) {
        return pedidoRepo.findByCliente_Id(idCliente);
    }


    @Transactional
    public void crearPedido(Cliente cliente, List<ItemCarrito> items) {

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado("PENDIENTE");

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        for (ItemCarrito i : items) {

            Producto producto = productoRepo.findById(i.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // ✅ Validar stock
            if (producto.getStock() < i.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            // ✅ Restar stock
            producto.setStock(producto.getStock() - i.getCantidad());
            productoRepo.save(producto);

            // ✅ Registrar inventario (SALIDA)
            Inventario mov = new Inventario();
            mov.setProducto(producto);
            mov.setTipoMovimiento(TipoMovimiento.SALIDA);
            mov.setCantidad(i.getCantidad());
            inventarioRepo.save(mov);

            // ✅ Crear detalle pedido
            DetallePedido d = new DetallePedido();
            d.setPedido(pedido);
            d.setProducto(producto);
            d.setCantidad(i.getCantidad());
            d.setPrecioUnitario(i.getPrecio());

            detalles.add(d);

            // total += subtotal
            total = total.add(i.getSubtotal());
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        pedidoRepo.save(pedido);
    }
    
    @Transactional
    public void ingresarStock(Long idProducto, int cantidad) {
        Producto p = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("No existe"));

        p.setStock(p.getStock() + cantidad);
        productoRepo.save(p);

        Inventario mov = new Inventario();
        mov.setProducto(p);
        mov.setTipoMovimiento(TipoMovimiento.ENTRADA);
        mov.setCantidad(cantidad);

        inventarioRepo.save(mov);
    }



}
