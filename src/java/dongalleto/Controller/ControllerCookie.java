package dongalleto.Controller;

import dongalleto.cqrs.CQRScookie;
import dongalleto.dao.DaoCookie;
import dongalleto.model.Cookie;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Eduardo Balderas
 */
public class ControllerCookie {

    static DaoCookie dao = new DaoCookie();
    static CQRScookie cqrC = new CQRScookie();

    public List<Cookie> getAll() throws ClassNotFoundException, SQLException, IOException {
        return dao.getAllCookies();
    }

    public Cookie updateStock(int id, int quantity) throws SQLException, ClassNotFoundException, IOException {
        return cqrC.updateStock(id, quantity);
    }

    public Cookie updateStatus(int id, String status) throws SQLException, ClassNotFoundException, IOException {
        return cqrC.updateStatus(id, status);  // Llamamos al CQRS para hacer la actualización
    }

}
