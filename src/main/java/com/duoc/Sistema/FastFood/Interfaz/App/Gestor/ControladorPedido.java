package com.duoc.Sistema.FastFood.Interfaz.App.Gestor;

import com.duoc.Sistema.FastFood.Interfaz.App.Gestor.DAO.EntregaDAO;
import com.duoc.Sistema.FastFood.Interfaz.App.Gestor.DAO.PedidoDAO;
import com.duoc.Sistema.FastFood.Interfaz.App.Gestor.DAO.RepartidorDAO;
import com.duoc.Sistema.FastFood.Interfaz.App.Model.EstadoPedido;
import com.duoc.Sistema.FastFood.Interfaz.App.Model.Pedido;
import com.duoc.Sistema.FastFood.Interfaz.App.Model.Repartidor;
import com.duoc.Sistema.FastFood.Interfaz.App.UI.VentanaListaPedidos;


import javax.swing.*;
import java.util.List;

/**
 * El controladorPedido permite gestionar la lista de pedidos y repartidores
 * Permite agregar pedidos y agregar repartidores, asignación automatica y manual (opcional)
 * Muestra el listado de pedidos ingresados desde la app o la base de datos.
 *
 */

public class ControladorPedido {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private VentanaListaPedidos ventanaListaPedidos;

    public void setVentanaListaPedidos(VentanaListaPedidos ventanaListaPedidos){
        this.ventanaListaPedidos = ventanaListaPedidos;}


    public void agregarPedido(Pedido pedido){
        boolean existe = pedidoDAO.listadoPedido().stream().
                anyMatch(p-> p.getId() == pedido.getId());

        if(!existe){pedidoDAO.guardar(pedido);
        actualizarLista();
        }
    }
    public void agregarRepartidor(Repartidor repartidor) {
        boolean existe = repartidorDAO.listadoRepartidor().stream()
                .anyMatch(r->r.getId() == repartidor.getId());

        if (!existe){repartidorDAO.guardar(repartidor);
        }
    }
    public List<Pedido> obtenerPedidos() {return pedidoDAO.listadoPedido();}
    public List<Repartidor> obtenerRepartidor() {return repartidorDAO.listadoRepartidor();}

    private void actualizarLista(){
        if(ventanaListaPedidos != null){
            SwingUtilities.invokeLater(()-> ventanaListaPedidos.cargarDatos(this));}
    }

    public void asignarAuto(Pedido pedido) {
        List<Repartidor> repartidores = repartidorDAO.listadoRepartidor();
        for (Repartidor repartidor : repartidores) {
            if (repartidor.estaDisponible() && repartidor.getTipoVehiculo() == pedido.getRequerimiento()) {
                asignarPedido(pedido,repartidor);
                return;
            }
        }
        throw new IllegalStateException("No hay repartidores disponibles para el pedido");
    }

    public void asignarManual(Pedido pedido, String nombreRepartidor) {
        Repartidor repartidor = obtenerRepartidor().stream()
                .filter(r -> r.getNombre().equalsIgnoreCase(nombreRepartidor))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Repartidor no encontrado"));
        if(!repartidor.estaDisponible()){
            throw new IllegalStateException("Repartidor no disponible");
        }
        asignarPedido(pedido, repartidor);
    }

    private void asignarPedido(Pedido pedido, Repartidor repartidor) {
        pedido.enReparto(repartidor.getNombre());
        repartidor.ocupar();
        repartidorDAO.actualizarDispo(repartidor.getNombre(), false);

        pedidoDAO.actEstadoPedido(pedido.getId(),EstadoPedido.EN_REPARTO, repartidor.getNombre());

        //GUARDA EN LA BBDD
        entregaDAO.guardar(pedido.getId(), repartidor.getId(), EstadoPedido.EN_REPARTO.name());
        actualizarLista();

        new Thread(() -> {
            try {
                long tiempo = pedido.calcularTiempoEntrega()*1000L;
                System.out.println("Tiempo de entrega: "+(tiempo/1000)+" segundos");

                Thread.sleep(tiempo);
                pedido.entregado();
                pedidoDAO.actEstadoPedido(pedido.getId(), EstadoPedido.ENTREGADO, pedido.getRepartidorAsignado());
                repartidor.liberar();
                repartidorDAO.actualizarDispo(repartidor.getNombre(), true);
                actualizarLista();

            } catch (InterruptedException exception) {
                exception.printStackTrace();}
        }).start();

    }

    public Pedido buscarPorId(int id) {
        return pedidoDAO.listadoPedido()
                .stream().filter(pedido -> pedido.getId() == id)
                .findFirst().orElse(null);
    }

}