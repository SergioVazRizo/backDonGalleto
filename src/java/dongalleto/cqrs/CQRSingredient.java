package dongalleto.cqrs;

import dongalleto.dao.DaoIngredient;
import dongalleto.model.Ingredient;
import java.io.IOException;
import java.sql.SQLException;

public class CQRSingredient {

    static DaoIngredient daoIngredient = new DaoIngredient();

    public Ingredient updateStock(int id, double quantity) throws SQLException, ClassNotFoundException, IOException {
        String validacion = validarStock(id, quantity);
        if (!validacion.equals("Todo correcto insercion exitosa")) {
            return null;  
        }
        return daoIngredient.updateStock(id, quantity); 
    }

    public String validarStock(int id, double quantity) {
        if (id <= 0) {
            return "El ID del ingrediente debe ser un valor positivo.";
        }
        if (quantity < -Double.MAX_VALUE && quantity > Double.MAX_VALUE) {
            return "La cantidad de stock no puede ser negativa o demasiado grande.";
        }
        return "Todo correcto insercion exitosa";
    }
}