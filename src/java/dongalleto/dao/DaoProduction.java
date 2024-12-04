package dongalleto.dao;

import dongalleto.bd.ConexionMySQL;
import dongalleto.model.Production;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoProduction {

    // Obtener todas las producciones
    public List<Production> getAllProductions() throws ClassNotFoundException, SQLException, IOException {
        String query = "SELECT * FROM production";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<Production> productions = new ArrayList<>();
        while (rs.next()) {
            Production production = new Production();
            production.setId(rs.getInt("id"));
            production.setCookieId(rs.getInt("cookie_id"));
            production.setProductionStatus(rs.getString("production_status"));
            production.setUnitsProduced(rs.getInt("units_produced"));

            productions.add(production);
        }

        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);
        return productions;
    }

    // Obtener producción por ID de galleta
    public Production getProductionByCookieId(int cookieId) throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT * FROM production WHERE cookie_id = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, cookieId);
        ResultSet rs = pstmt.executeQuery();

        Production production = null;
        if (rs.next()) {
            production = new Production();
            production.setId(rs.getInt("id"));
            production.setCookieId(rs.getInt("cookie_id"));
            production.setProductionStatus(rs.getString("production_status"));
            production.setUnitsProduced(rs.getInt("units_produced"));
        }

        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);
        return production;
    }

    public Production updateProductionStatus(int cookieId, String newStatus) throws SQLException, ClassNotFoundException, IOException {
        String updateQuery = "UPDATE production SET production_status = ? WHERE cookie_id = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        try {
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, newStatus);
            updateStmt.setInt(2, cookieId);
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                // Recuperar el registro actualizado
                return getProductionByCookieId(cookieId);
            }
        } finally {
            connMySQL.cerrarConexion(conn);
        }
        return null;
    }

    // Modificar el método completeProduction para incluir la actualización de stock
    public Production completeProduction(int cookieId) throws SQLException, ClassNotFoundException, IOException {
        Production production = getProductionByCookieId(cookieId);
        if (production != null) {
            // Aquí deberías implementar la lógica para actualizar el stock
            // Este es un ejemplo simplificado, deberás adaptarlo a tu modelo de negocio
            int newStock = production.getUnitsProduced(); // Ejemplo de cómo podría incrementarse el stock

            // Actualizar el estado a completado
            String updateQuery = "UPDATE production SET production_status = 'completada' WHERE cookie_id = ?";
            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.abrirConexion();

            try {
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, cookieId);
                updateStmt.executeUpdate();

                // Añadir un campo temporal para el nuevo stock (si es necesario en tu modelo)
                production.setNewStock(newStock);
                production.setProductionStatus("completada");
            } finally {
                connMySQL.cerrarConexion(conn);
            }
        }
        return production;
    }

}