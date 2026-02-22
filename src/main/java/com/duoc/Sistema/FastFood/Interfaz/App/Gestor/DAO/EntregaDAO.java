package com.duoc.Sistema.FastFood.Interfaz.App.Gestor.DAO;

import com.duoc.Sistema.FastFood.Interfaz.App.Conexion.ConexionBBDD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Permite guardar información en la base de datos sobre los pedidos ingresados en la interfaz.
 */


public class EntregaDAO {

    public void guardar(int idPedido, int idRepartidor, String estado){
        String sql = "INSERT INTO entrega (id_pedido,id_repartidor,estado_entrega) VALUES (?,?,?)";

        try(Connection connection = ConexionBBDD.obtenerConexion();
            PreparedStatement stnt = connection.prepareStatement(sql)){


            stnt.setInt(1,idPedido);
            stnt.setInt(2,idRepartidor);
            stnt.setString(3, estado);
            stnt.executeUpdate();
        }catch (SQLException exception){exception.printStackTrace();}
    }
}
