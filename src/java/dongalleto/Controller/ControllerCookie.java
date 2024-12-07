package dongalleto.Controller;

import dongalleto.appservice.CookieExternoAppService;
import dongalleto.cqrs.CQRScookie;
import dongalleto.dao.DaoCookie;
import dongalleto.model.Cookie;
import dongalleto.modelView.ModelViewCookie;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo Balderas
 */
public class ControllerCookie {

    static DaoCookie dao = new DaoCookie();
    static CQRScookie cqrC = new CQRScookie();
    static CookieExternoAppService lc = new CookieExternoAppService();

    public List<Cookie> getAll() throws ClassNotFoundException, SQLException, IOException {
        return dao.getAllCookies();
    }

    public Cookie updateStock(int id, int quantity) throws SQLException, ClassNotFoundException, IOException {
        try {
            return cqrC.updateStock(id, quantity);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e.getMessage()); // Transformamos el mensaje para que sea manejado correctamente
        }
    }

    public Cookie updateStatus(int id, String status) throws SQLException, ClassNotFoundException, IOException {
        return cqrC.updateStatus(id, status);  // Llamamos al CQRS para hacer la actualización
    }

    public List<ModelViewCookie> getAllPublic() {
        try {
            List<Cookie> cookie = dao.getAllCookies();
            List<ModelViewCookie> cookieView = new ArrayList<>();
            for (Cookie c : cookie) {
                ModelViewCookie item = new ModelViewCookie(c.getId(), c.getName(), c.getRecipeId(), c.getDescription(), c.getStatus(), c.getUnitPrice(), c.getPackage500gPrice(), c.getPackage1000gPrice(), c.getPricePerGram(), c.getStock(), c.getWeightPerUnit());
                cookieView.add(item);
            }
            return cookieView;

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(ControllerCookie.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    public List<ModelViewCookie> getCookiesAll() throws SQLException, ClassNotFoundException, IOException {
        List<ModelViewCookie> cookiesLocal = getAllPublic();
        List<ModelViewCookie> externalCookies = lc.getAllLibrosExtern();
        cookiesLocal.addAll(externalCookies);
        return cookiesLocal;
    }

}
