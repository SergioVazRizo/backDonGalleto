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
        return ds.validateSale(cookieId, quantity, saleType);
    }

     public boolean cancelSale(int saleId) throws SQLException, ClassNotFoundException, IOException {
    return ds.cancelSale(saleId);  // Llama al método del DAO para cancelar la venta
}


}
