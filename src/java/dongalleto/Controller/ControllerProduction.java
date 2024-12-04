<<<<<<< HEAD
package dongalleto.Controller;
=======
package dongalleto.controller;
>>>>>>> 10c5db2ac9177f958ccd39203289b093f15fe90d

import dongalleto.dao.DaoProduction;
import dongalleto.model.Production;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControllerProduction {

    static DaoProduction dao = new DaoProduction();

    // Obtener todas las producciones
    public List<Production> getAllProductions() throws ClassNotFoundException, SQLException, IOException {
        return dao.getAllProductions();
    }

    // Obtener producción por ID de galleta
    public Production getProductionByCookieId(int cookieId) throws SQLException, ClassNotFoundException, IOException {
        return dao.getProductionByCookieId(cookieId);
    }

    // Método para actualizar el estado de producción
    public Production updateProductionStatus(int cookieId, String newStatus) throws SQLException, ClassNotFoundException, IOException {
        return dao.updateProductionStatus(cookieId, newStatus);
    }

    // El método completeProduction se mantiene igual
    public Production completeProduction(int cookieId) throws SQLException, ClassNotFoundException, IOException {
        return dao.completeProduction(cookieId);
    }
<<<<<<< HEAD
    
    public int addCookieProduction(Production production) throws ClassNotFoundException, SQLException, IOException{
        return dao.addProduction(production);
    }
=======
>>>>>>> 10c5db2ac9177f958ccd39203289b093f15fe90d
}