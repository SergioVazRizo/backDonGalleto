/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dongalleto.bd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Usuario
 */
public class ConexionMySQL {
        String url = "jdbc:mysql://localhost:3306/don_galleto";
    String user = "root";
    String password = "Rocha3107";

    public Connection abrirConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        

        Connection conexion = DriverManager.getConnection(url, user, password);

        return conexion;
    }

    public void cerrarConexion(Connection conexion) throws IOException, SQLException {
        if(conexion!=null){
            conexion.close();
        }
        
    }
}
