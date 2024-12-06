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

    public Production getProductionById(int productionId) throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT * FROM production WHERE id = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, productionId);
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

    public Production updateProductionStatus(int productionId, String newStatus) throws SQLException, ClassNotFoundException, IOException {
        String updateQuery = "UPDATE production SET production_status = ? WHERE id = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        try {
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, newStatus);
            updateStmt.setInt(2, productionId);
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                return getProductionById(productionId);
            }
        } finally {
            connMySQL.cerrarConexion(conn);
        }
        return null;
    }

    public Production completeProduction(int productionId) throws SQLException, ClassNotFoundException, IOException {
        Production production = getProductionById(productionId);

        if (production != null) {
            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.abrirConexion();

            int currentStock = getCurrentStock(production.getCookieId(), conn);
            int unitsProduced = 750;
            int newStock = currentStock + unitsProduced;

            updateStock(production.getCookieId(), newStock, conn);

            String updateQuery = "UPDATE production SET production_status = 'lista', units_produced = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, unitsProduced);
            updateStmt.setInt(2, productionId);
            updateStmt.executeUpdate();

            production.setNewStock(newStock);
            production.setProductionStatus("lista");

            updateStmt.close();
            connMySQL.cerrarConexion(conn);

            return production;
        }

        return null;
    }

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
        return 0;
    }

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

        PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, production.getCookieId());
        pstmt.setString(2, production.getProductionStatus());
        pstmt.setInt(3, production.getUnitsProduced());

        pstmt.executeUpdate();

        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int generatedId = -1;
        if (generatedKeys.next()) {
            generatedId = generatedKeys.getInt(1);
        }

        generatedKeys.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);

        return generatedId;
    }

}
