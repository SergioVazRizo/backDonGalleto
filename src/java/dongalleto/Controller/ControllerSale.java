package dongalleto.Controller;

import com.google.gson.JsonObject;
import dongalleto.cqrs.CQRSSale;
import dongalleto.dao.DaoSale;
import dongalleto.model.Sale;
import dongalleto.model.SaleItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Eduardo Balderas
 */
public class ControllerSale {

    static DaoSale ds = new DaoSale();
    static CQRSSale cqrss = new CQRSSale();

    public List<Sale> getAll() throws ClassNotFoundException, SQLException, IOException {
        return ds.getAllSales();
    }

    public Sale createSale(Sale sale) throws SQLException, ClassNotFoundException, IOException {

        return cqrss.createSale(sale);
    }
    
      public JsonObject validateSale(int cookieId, int quantity, String saleType) throws SQLException, ClassNotFoundException, IOException {
        // Obtener la venta validada
        SaleItem saleItem = ds.validateSale(cookieId, quantity, saleType);
        
        // Crear el JsonObject para la respuesta
        JsonObject validationResult = new JsonObject();
        
        // Agregar la información de la validación
        validationResult.addProperty("isValid", saleItem != null); // Si se encuentra el SaleItem, es válida
        validationResult.addProperty("message", saleItem != null ? "Venta válida" : "Venta no válida");
        validationResult.addProperty("actualQuantity", saleItem != null ? saleItem.getQuantity() : 0);
        validationResult.addProperty("total", saleItem != null ? saleItem.getSubtotal() : 0);
        
        // Agregar detalles del SaleItem (si la venta es válida)
        if (saleItem != null) {
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("cookieId", saleItem.getCookieId());
            itemJson.addProperty("cookieName", saleItem.getCookieName()); // Suponiendo que lo tienes en el SaleItem
            itemJson.addProperty("quantity", saleItem.getQuantity());
            itemJson.addProperty("pricePerUnit", saleItem.getPricePerUnit()); // Suponiendo que lo tienes en el SaleItem
            itemJson.addProperty("subtotal", saleItem.getSubtotal());

            validationResult.add("item", itemJson);
        }

        return validationResult;
    }

}
