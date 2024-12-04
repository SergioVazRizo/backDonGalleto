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

    public Production completeProduction(int cookieId) throws SQLException, ClassNotFoundException, IOException {
        // Obtener la producción pendiente por cookieId
        Production production = getProductionByCookieId(cookieId);

        if (production != null) {
            // Conectar a la base de datos
            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.abrirConexion();

            // Obtener el stock actual de las galletas
            int currentStock = getCurrentStock(cookieId, conn);

            // Aquí asignamos 750 unidades por defecto
            int unitsProduced = 750; // Unidades producidas por defecto por receta

            // Calcular el nuevo stock sumando las unidades producidas
            int newStock = currentStock + unitsProduced;

            // Actualizar el stock de galletas
            updateStock(cookieId, newStock, conn);

            // Actualizar el estado de la producción a 'completado'
            String updateQuery = "UPDATE production SET production_status = 'lista', units_produced = ? WHERE cookie_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, unitsProduced); // Asignamos las 750 unidades producidas
            updateStmt.setInt(2, cookieId);
            updateStmt.executeUpdate();

            // Establecer el nuevo estado y stock en el objeto de producción
            production.setNewStock(newStock);
            production.setProductionStatus("lista");

            // Cerrar conexiones
            updateStmt.close();
            connMySQL.cerrarConexion(conn);

            return production; // Retornar el objeto de producción con el nuevo stock y estado
        }

        return null; // Si no se encuentra la producción
    }

// Método para obtener el stock actual de las galletas
    private int getCurrentStock(int cookieId, Connection conn) throws SQLException {
        String query = "SELECT stock FROM cookies WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, cookieId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("stock");
        }

        rs.close();
        pstmt.close();
        return 0; // Si no se encuentra el stock, retornar 0
    }

// Método para actualizar el stock de galletas
    private void updateStock(int cookieId, int newStock, Connection conn) throws SQLException {
        String updateQuery = "UPDATE cookies SET stock = ? WHERE id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setInt(1, newStock);
        updateStmt.setInt(2, cookieId);
        updateStmt.executeUpdate();
        updateStmt.close();
    }

    public int addProduction(Production production) throws ClassNotFoundException, SQLException, IOException {
        String query = "INSERT INTO production (cookie_id, production_status, units_produced) VALUES (?, ?, ?)";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        // Especificar que queremos obtener las claves generadas automáticamente
        PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

        // Configurar los parámetros de la consulta
        pstmt.setInt(1, production.getCookieId());
        pstmt.setString(2, production.getProductionStatus());
        pstmt.setInt(3, production.getUnitsProduced());

        // Ejecutar la inserción
        pstmt.executeUpdate();

        // Obtener la clave generada (ID)
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int generatedId = -1; // Valor predeterminado si no se genera un ID
        if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1); // Obtener el primer campo como ID
        }

        // Cerrar conexiones
        generatedKeys.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);

        // Devolver el ID generado
        return generatedId;
    }

}
