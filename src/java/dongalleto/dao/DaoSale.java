package dongalleto.dao;

import com.google.gson.JsonObject;
import dongalleto.bd.ConexionMySQL;
import dongalleto.model.Sale;
import dongalleto.model.SaleItem;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoSale {

    public List<Sale> getAllSales() throws ClassNotFoundException, SQLException, IOException {
        // Consulta SQL a la vista
        String query = "SELECT * FROM vw_sales_details";

        // Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        // Preparar la consulta
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        // Lista para almacenar las ventas
        List<Sale> sales = new ArrayList<>();
        Sale currentSale = null;

        // Iterar sobre los resultados de la vista
        while (rs.next()) {
            int saleId = rs.getInt("sale_id");

            // Si cambiamos de venta, creamos una nueva instancia
            if (currentSale == null || currentSale.getId() != saleId) {
                if (currentSale != null) {
                    sales.add(currentSale);
                }
                currentSale = new Sale();
                currentSale.setId(saleId);
                currentSale.setDate(rs.getString("sale_date"));
                currentSale.setItems(new ArrayList<>());
                currentSale.setTotal(rs.getDouble("sale_total"));
            }

            // Crear el ítem de venta y agregarlo a la venta actual
            SaleItem saleItem = new SaleItem();
            saleItem.setCookieId(rs.getInt("item_cookie_id"));
            saleItem.setCookieName(rs.getString("cookie_name"));
            saleItem.setQuantity(rs.getInt("item_quantity"));
            saleItem.setPricePerUnit(rs.getDouble("item_price_per_unit"));
            saleItem.setSubtotal(rs.getDouble("item_subtotal"));

            currentSale.getItems().add(saleItem);
        }

        // Agregar la última venta procesada
        if (currentSale != null) {
            sales.add(currentSale);
        }

        // Cerrar conexiones
        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);

        return sales;
    }

    public Sale createSale(Sale sale) throws SQLException, ClassNotFoundException, IOException {
        // Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn.setAutoCommit(false); // Deshabilitar auto-commit para manejo manual de transacciones

            // Insertar en la tabla 'sales'
            String insertSaleQuery = "INSERT INTO sales (date, total) VALUES (?, ?)";
            pstmt = conn.prepareStatement(insertSaleQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));  // Fecha actual
            pstmt.setDouble(2, sale.getTotal());
            pstmt.executeUpdate();

            // Obtener el id generado para la venta
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int saleId = generatedKeys.getInt(1);
                sale.setId(saleId);
            }

            // Insertar en la tabla 'sale_items'
            String insertSaleItemsQuery = "INSERT INTO sale_items (sale_id, cookie_id, quantity, sale_type, total) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertSaleItemsQuery);

            // Iterar sobre los items de la venta
            for (SaleItem item : sale.getItems()) {
                pstmt.setInt(1, sale.getId());
                pstmt.setInt(2, item.getCookieId());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setString(4, item.getSaleType());  // Aquí se maneja el saleType como String
                pstmt.setDouble(5, item.getSubtotal());
                pstmt.addBatch();  // Usamos batch para mejorar el rendimiento
            }

            // Ejecutar la inserción de todos los ítems
            pstmt.executeBatch();

            // Confirmar la transacción
            conn.commit();

            // Cerrar las conexiones
            pstmt.close();
            connMySQL.cerrarConexion(conn);

            return sale;  // Devolver la venta creada con su id

        } catch (SQLException ex) {
            // En caso de error, revertir la transacción
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error al crear la venta: " + ex.getMessage());
        }
    }

    public JsonObject validateSale(int cookieId, int quantity, String saleType) throws SQLException, ClassNotFoundException, IOException {
        // Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Crear el objeto de respuesta
        JsonObject response = new JsonObject();

        try {
            // Consultar el stock disponible de la galleta
            String query = "SELECT stock, unit_price, weight_per_unit, package_500g_price, package_1000g_price FROM cookies WHERE id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, cookieId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                double stock = rs.getDouble("stock");
                double pricePerUnit = rs.getDouble("unit_price");
                double weightPerUnit = rs.getDouble("weight_per_unit");
                double package500gPrice = rs.getDouble("package_500g_price");
                double package1000gPrice = rs.getDouble("package_1000g_price");

                // Determinar el tipo de venta y validar la cantidad
                double totalAmount = 0;
                boolean isValid = true;
                String message = "Venta válida";

                switch (saleType) {
                    case "UNIT":
                        if (quantity > stock) {
                            isValid = false;
                            message = "Stock insuficiente para la venta";
                        }
                        totalAmount = quantity * pricePerUnit;
                        break;
                    case "WEIGHT":
                        if (quantity > stock * weightPerUnit) {
                            isValid = false;
                            message = "Stock insuficiente para la venta";
                        }
                        totalAmount = quantity * pricePerUnit;  // Supongamos que el precio por unidad es el mismo
                        break;
                    case "PACKAGE_500":
                        if (quantity > stock * 500) {
                            isValid = false;
                            message = "Stock insuficiente para la venta";
                        }
                        totalAmount = quantity * package500gPrice;
                        break;
                    case "PACKAGE_1000":
                        if (quantity > stock * 1000) {
                            isValid = false;
                            message = "Stock insuficiente para la venta";
                        }
                        totalAmount = quantity * package1000gPrice;
                        break;
                    case "AMOUNT":
                        // Aquí necesitarías calcular según el monto de venta
                        break;
                    default:
                        isValid = false;
                        message = "Tipo de venta no válido";
                        break;
                }

                // Preparar la respuesta
                response.addProperty("isValid", isValid);
                response.addProperty("message", message);
                response.addProperty("actualQuantity", stock);
                response.addProperty("total", totalAmount);

            } else {
                response.addProperty("isValid", false);
                response.addProperty("message", "Galleta no encontrada");
            }
        } finally {
            // Cerrar conexiones
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            connMySQL.cerrarConexion(conn);
        }

        return response;
    }

    
    
    
    
    
    
    
    public boolean cancelSale(int saleId) throws SQLException, ClassNotFoundException, IOException {
    // Conectar a la base de datos
    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.abrirConexion();
    PreparedStatement pstmt = null;

    try {
        // Comenzar la transacción
        conn.setAutoCommit(false);

        // Actualizar el estado de la venta para marcarla como cancelada
        String updateSaleQuery = "UPDATE sales SET status = 'CANCELLED' WHERE id = ?";
        pstmt = conn.prepareStatement(updateSaleQuery);
        pstmt.setInt(1, saleId);
        int rowsAffected = pstmt.executeUpdate();

        // Confirmar la transacción
        conn.commit();

        return rowsAffected > 0;  // Retorna true si la venta fue cancelada

    } catch (SQLException ex) {
        if (conn != null) {
            conn.rollback();  // Revertir la transacción en caso de error
        }
        throw new SQLException("Error al cancelar la venta: " + ex.getMessage());
    } finally {
        // Cerrar conexiones
        if (pstmt != null) {
            pstmt.close();
        }
        connMySQL.cerrarConexion(conn);
    }
}


}
