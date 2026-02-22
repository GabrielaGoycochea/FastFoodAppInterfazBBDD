package com.duoc.Sistema.FastFood.Interfaz.App.UI;

/**
 * Ventana principal, muestra las primeras opciones para gestionar.
 */

import com.duoc.Sistema.FastFood.Interfaz.App.Gestor.ControladorPedido;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private ControladorPedido controladorPedido;

    public VentanaPrincipal (ControladorPedido controladorPedido) {
        this.controladorPedido = controladorPedido;

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnRegistrar = new JButton("Registrar Pedido");
        JButton btnListar = new JButton("Lista de Pedidos");
        JButton btnAsignar = new JButton("Asignar Repartidor e Iniciar Entrega");
        JButton btnSalir = new JButton("Salir");

        btnRegistrar.addActionListener(e -> new VentanaRegistroPedido(controladorPedido));
        btnListar.addActionListener(e -> new VentanaListaPedidos(controladorPedido));
        btnAsignar.addActionListener(e -> new VentanaAsignarRepartidor(controladorPedido));
        btnSalir.addActionListener(e -> System.exit(0));


        setLayout(new GridLayout(4,1));
        add(btnRegistrar);
        add(btnListar);
        add(btnAsignar);
        add(btnSalir);

        setVisible(true);


    }

}
