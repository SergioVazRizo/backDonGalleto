/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dongalleto.dao;

import dongalleto.bd.ConexionMySQL;
import dongalleto.model.Cookie;
import java.io.IOException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo Balderas
 */
public class DaoCookie {

    public List<Cookie> getAllCookies() throws ClassNotFoundException, SQLException, IOException {
        // Consulta SQL
        String query = "SELECT * FROM vw_cookie_details";

        // Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        // Preparar la consulta
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        // Lista para almacenar las cookies
        List<Cookie> cookies = new ArrayList<>();

        // Iterar sobre los resultados
        while (rs.next()) {
            Cookie cookie = new Cookie();
            cookie.setId(rs.getInt("id"));
            cookie.setName(rs.getString("name"));
            cookie.setRecipeId(rs.getInt("recipe_id"));
            cookie.setDescription(rs.getString("description"));
            cookie.setStatus(rs.getString("status"));
            cookie.setStock(rs.getInt("stock"));
            cookie.setWeightPerUnit(rs.getDouble("weight_per_unit"));
            cookie.setUnitPrice(rs.getDouble("unit_price"));
            cookie.setPackage500gPrice(rs.getDouble("package_500g_price"));
            cookie.setPackage1000gPrice(rs.getDouble("package_1000g_price"));
            cookie.setPricePerGram(rs.getDouble("price_per_gram"));

            cookies.add(cookie);
        }

        // Cerrar conexiones
        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);

        return cookies;
    }

    public Cookie updateStock(int id, int quantity) throws SQLException, ClassNotFoundException, IOException {
        Cookie cookie = null;
        ConexionMySQL connMysql = new ConexionMySQL();
        String updateQuery = "UPDATE cookies SET stock = stock + ? WHERE id = ? AND (stock + ?) >= 0"; // Aseguramos que no quede negativo.
        String selectQuery = "SELECT * FROM cookies WHERE id = ?";
        Connection conn = connMysql.abrirConexion();

        try {
            conn.setAutoCommit(false);
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, id);
            updateStmt.setInt(3, quantity); // Verificación para evitar stock negativo
            int rowsUpdated = updateStmt.executeUpdate();
            updateStmt.close();

            if (rowsUpdated == 0) {
                throw new SQLException("No se pudo actualizar el stock. Puede que la cookie no exista o el stock sea insuficiente.");
            }

            // Consultar la cookie actualizada
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                cookie = new Cookie();
                cookie.setId(rs.getInt("id"));
                cookie.setName(rs.getString("name"));
                cookie.setRecipeId(rs.getInt("recipe_id"));
                cookie.setDescription(rs.getString("description"));
                cookie.setStatus(rs.getString("status"));
                cookie.setUnitPrice(rs.getDouble("unit_price"));
                cookie.setPackage500gPrice(rs.getDouble("package_500g_price"));
                cookie.setPackage1000gPrice(rs.getDouble("package_1000g_price"));
                cookie.setPricePerGram(rs.getDouble("price_per_gram"));
                cookie.setStock(rs.getInt("stock"));
                cookie.setWeightPerUnit(rs.getDouble("weight_per_unit"));

                // Verificar si el stock es 0 y actualizar el estado
                if (cookie.getStock() == 0) {
                    updateStatus(id, "Agotado");
                    cookie.setStatus("Agotado"); // Actualiza el objeto cookie en memoria también
                }
            }
            rs.close();
            selectStmt.close();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            connMysql.cerrarConexion(conn);
        }

        return cookie;
    }

    public Cookie updateStatus(int id, String status) throws SQLException, ClassNotFoundException, IOException {
        Cookie cookie = null;
        ConexionMySQL connMysql = new ConexionMySQL();
        String updateQuery = "UPDATE cookies SET status = ? WHERE id = ?";
        String selectQuery = "SELECT * FROM cookies WHERE id = ?";

        Connection conn = connMysql.abrirConexion();

        try {
            conn.setAutoCommit(false);
            // Actualizar el estado de la cookie
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, status);
            updateStmt.setInt(2, id);
            int rowsUpdated = updateStmt.executeUpdate();
            updateStmt.close();

            if (rowsUpdated == 0) {
                // Si no se actualizó ninguna fila (no se encontró la cookie), lanzar excepción
                throw new SQLException("No se encontró la cookie con ID " + id);
            }

            // Consultar la cookie actualizada
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                cookie = new Cookie();
                cookie.setId(rs.getInt("id"));
                cookie.setName(rs.getString("name"));
                cookie.setRecipeId(rs.getInt("recipe_id"));
                cookie.setDescription(rs.getString("description"));
                cookie.setStatus(rs.getString("status"));
                cookie.setStock(rs.getInt("stock"));
                cookie.setWeightPerUnit(rs.getDouble("weight_per_unit"));
                cookie.setUnitPrice(rs.getDouble("unit_price"));
                cookie.setPackage500gPrice(rs.getDouble("package_500g_price"));
                cookie.setPackage1000gPrice(rs.getDouble("package_1000g_price"));
                cookie.setPricePerGram(rs.getDouble("price_per_gram"));
            }

            rs.close();
            selectStmt.close();

            // Confirmar la transacción
            conn.commit();
        } catch (Exception e) {
            // Revertir la transacción si algo falla
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            connMysql.cerrarConexion(conn);
        }

        // Retornar la cookie actualizada
        return cookie;
    }

}
