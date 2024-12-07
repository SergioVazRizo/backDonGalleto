package dongalleto.cqrs;

import dongalleto.dao.DaoCookie;
import dongalleto.model.Cookie;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Eduardo Balderas
 */
public class CQRScookie {

    static DaoCookie daoCookie = new DaoCookie();

    public Cookie updateStock(int id, int quantity) throws SQLException, ClassNotFoundException, IOException {
    String validacion = validarCookie(id, quantity);
    if (!validacion.equals("Todo correcto insercion exitosa")) {
        throw new IllegalArgumentException(validacion); // Lanza una excepción con el mensaje de validación
    }
    return daoCookie.updateStock(id, quantity);
}


    public String validarCookie(int id, int quantity) {
    if (id <= 0) {
        return "El ID de la cookie debe ser un valor positivo.";
    }
    // Permitimos valores negativos en la cantidad.
    return "Todo correcto insercion exitosa";
}

  
    public Cookie updateStatus(int id, String status) throws SQLException, ClassNotFoundException, IOException {
        String validacion = validarStatus(id, status);
        if (!validacion.equals("Todo correcto insercion exitosa")) {
            return null; 
        }
        return daoCookie.updateStatus(id, status);  
    }

   
    public String validarStatus(int id, String status) {
        if (id <= 0) {
            return "El ID de la cookie debe ser un valor positivo.";
        }

        if (!status.equals("Existencia") && !status.equals("Agotado")) {
            return "El estado debe ser 'Existencia' o 'Agotado'.";
        }

        return "Todo correcto insercion exitosa";  // Validación exitosa
    }
}
