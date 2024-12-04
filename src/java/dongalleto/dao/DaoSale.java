package dongalleto.dao;

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

    public SaleItem validateSale(int cookieId, int quantity, String saleType) throws SQLException, ClassNotFoundException, IOException {
        // Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        // Consulta para obtener el stock y el precio de la galleta
        String query = "SELECT stock, unit_price, weight_per_unit, package_500g_price, package_1000g_price FROM cookies WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, cookieId);

        // Ejecutar la consulta
        ResultSet rs = pstmt.executeQuery();

        // Validar si la galleta existe
        if (!rs.next()) {
            rs.close();
            pstmt.close();
            connMySQL.cerrarConexion(conn);
            throw new SQLException("Cookie not found.");
        }

        // Obtener los detalles de la galleta
        double stock = rs.getDouble("stock");
        double unitPrice = rs.getDouble("unit_price");
        double weightPerUnit = rs.getDouble("weight_per_unit");
        double package500Price = rs.getDouble("package_500g_price");
        double package1000Price = rs.getDouble("package_1000g_price");

        // Variables de cantidad y precio total
        double total = 0;
        boolean isValid = false;
        String message = "Sale is valid.";
        int actualQuantity = quantity;

        // Lógica de validación según el tipo de venta
        switch (saleType) {
            case "UNIT":
                // Validar si hay suficiente stock en unidades
                if (quantity <= stock) {
                    total = unitPrice * quantity;
                    isValid = true;
                } else {
                    message = "Not enough stock for unit sale.";
                    isValid = false;
                    actualQuantity = (int) stock;  // Ajustar la cantidad disponible
                }
                break;

            case "WEIGHT":
                // Validar si hay suficiente stock en peso
                if (quantity <= stock * weightPerUnit) {
                    total = unitPrice * quantity;  // Calcula precio por peso
                    isValid = true;
                } else {
                    message = "Not enough stock for weight sale.";
                    isValid = false;
                    actualQuantity = (int) (stock * weightPerUnit);  // Ajustar la cantidad disponible
                }
                break;

            case "PACKAGE_500":
                // Validar si hay suficiente stock en paquetes de 500g
                if (quantity <= stock / 0.5) {
                    total = package500Price * quantity;  // Calcula precio por paquete de 500g
                    isValid = true;
                } else {
                    message = "Not enough stock for 500g package sale.";
                    isValid = false;
                    actualQuantity = (int) (stock / 0.5);  // Ajustar la cantidad disponible
                }
                break;

            case "PACKAGE_1000":
                // Validar si hay suficiente stock en paquetes de 1000g
                if (quantity <= stock / 1.0) {
                    total = package1000Price * quantity;  // Calcula precio por paquete de 1000g
                    isValid = true;
                } else {
                    message = "Not enough stock for 1000g package sale.";
                    isValid = false;
                    actualQuantity = (int) (stock / 1.0);  // Ajustar la cantidad disponible
                }
                break;

            case "AMOUNT":
                // Si es por cantidad, no validamos stock, solo calculamos el total
                total = unitPrice * quantity;
                isValid = true;
                break;

            default:
                message = "Invalid sale type.";
                isValid = false;
                break;
        }

        // Crear el objeto SaleItem con la validación
        SaleItem saleItem = new SaleItem();
        saleItem.setCookieId(cookieId);
        saleItem.setQuantity(actualQuantity);
        saleItem.setSaleType(saleType);
        saleItem.setSubtotal(total);
        saleItem.setCookieName("Cookie name");  // Asumiendo que tienes el nombre de la cookie disponible

        // Cerrar conexiones
        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);

        // Devolver el objeto SaleItem con los resultados
        return saleItem;
    }

    public static void main(String[] args) {
        DaoSale daoSale = new DaoSale();

        // Definir los parámetros de la venta para la validación
        int cookieId = 1; // ID de la galleta (ajusta según el ID real)
        int quantity = 10; // Cantidad solicitada
        String saleType = "UNIT"; // Tipo de venta (puede ser "UNIT", "WEIGHT", "PACKAGE_500", "PACKAGE_1000", "AMOUNT")

        try {
            // Validar la venta
            SaleItem saleItem = daoSale.validateSale(cookieId, quantity, saleType);

            // Mostrar los resultados de la validación
            System.out.println("Sale Item Validated:");
            System.out.println("Cookie ID: " + saleItem.getCookieId());
            System.out.println("Quantity: " + saleItem.getQuantity());
            System.out.println("Sale Type: " + saleItem.getSaleType());
            System.out.println("Subtotal: " + saleItem.getSubtotal());
            System.out.println("Message: " + (saleItem.getQuantity() == quantity ? "Sale is valid" : "Sale is not valid"));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            // Manejar excepciones
            System.err.println("Error during sale validation: " + e.getMessage());
        }
    }
}
