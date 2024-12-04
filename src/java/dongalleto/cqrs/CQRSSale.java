/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dongalleto.cqrs;

import dongalleto.dao.DaoSale;
import dongalleto.model.Sale;
import dongalleto.model.SaleItem;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Eduardo Balderas
 */
public class CQRSSale {
     static DaoSale daoSale = new DaoSale(); // Usamos el DAO para interactuar con la base de datos

    // Método para crear una nueva venta
    public Sale createSale(Sale sale) throws SQLException, ClassNotFoundException, IOException {
        String validacion = validarVenta(sale);
        if (!validacion.equals("Todo correcto, inserción exitosa")) {
            return null;  // Si no es válido, retornamos null
        }
        return daoSale.createSale(sale);  // Devuelve la venta creada
    }

    // Método para validar la venta
    public String validarVenta(Sale sale) {
        if (sale == null || sale.getItems().isEmpty()) {
            return "La venta debe contener al menos un ítem.";
        }

        // Validar los ítems de la venta
        for (SaleItem item : sale.getItems()) {
            String itemValidacion = validarSaleItem(item);
            if (!itemValidacion.equals("Todo correcto, inserción exitosa")) {
                return itemValidacion;  // Si algún ítem no es válido, retornamos el mensaje de error
            }
        }

        // Validación exitosa
        return "Todo correcto, inserción exitosa";
    }

    // Método para validar un ítem de la venta
    public String validarSaleItem(SaleItem item) {
        if (item.getCookieId() <= 0) {
            return "El ID de la galleta debe ser un valor positivo.";
        }

        if (item.getQuantity() <= 0) {
            return "La cantidad debe ser un valor positivo.";
        }

        if (item.getSubtotal() <= 0) {
            return "El subtotal debe ser un valor positivo.";
        }

        if (item.getPricePerUnit() <= 0) {
            return "El precio por unidad debe ser un valor positivo.";
        }

        return "Todo correcto, inserción exitosa";  // Validación exitosa
    }
}
