package com.superpet.ProyectoSuperpet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
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

    public List<Pedido> listarPedidosPorCliente(Long idCliente) {
        return pedidoRepo.findByCliente_Id(idCliente);
    }

    
    
    private void cancelarPedido(Pedido pedido) {
        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepo.save(pedido);
    }


    
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelarPedidosExpirados() {

        List<Pedido> expirados = pedidoRepo
            .findByEstadoAndFechaLimitePagoBefore(
                EstadoPedido.PENDIENTE,
                LocalDateTime.now()
            );

        expirados.forEach(this::cancelarPedido);
    }

    


    @Transactional
    public void crearPedido(Cliente cliente, List<ItemCarrito> items) {

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setFechaLimitePago(LocalDateTime.now().plusMinutes(15));

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        for (ItemCarrito i : items) {

            Producto producto = productoRepo.findById(i.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            //VALIDAR stock disponible
            if (producto.getStock() < i.getCantidad()) {
                throw new RuntimeException("Stock insuficiente: " + producto.getNombre());
            }

            //NO se descuenta stock aquí
            DetallePedido d = new DetallePedido();
            d.setPedido(pedido);
            d.setProducto(producto);
            d.setCantidad(i.getCantidad());
            d.setPrecioUnitario(i.getPrecio());

            detalles.add(d);
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
      
    
    public Pedido buscarPedidoCliente(Long idPedido, Cliente cliente) {

        Pedido pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no existe"));

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Acceso denegado");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new RuntimeException("Pedido no disponible para pago");
        }

        return pedido;
    }


    @Transactional
    public void pagarPedido(Long idPedido, Cliente cliente) {

        Pedido pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no existe"));

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Acceso denegado");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new RuntimeException("Pedido no válido");
        }

        if (pedido.getFechaLimitePago().isBefore(LocalDateTime.now())) {
            cancelarPedido(pedido);
            throw new RuntimeException("Tiempo de pago expirado");
        }

        //DESCONTAR STOCK 
        for (DetallePedido d : pedido.getDetalles()) {

            Producto p = d.getProducto();

            if (p.getStock() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }

            p.setStock(p.getStock() - d.getCantidad());
            productoRepo.save(p);

            Inventario mov = new Inventario();
            mov.setProducto(p);
            mov.setTipoMovimiento(TipoMovimiento.SALIDA);
            mov.setCantidad(d.getCantidad());
            inventarioRepo.save(mov);
        }

        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepo.save(pedido);
    }
    

}
