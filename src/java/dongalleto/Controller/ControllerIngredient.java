package dongalleto.Controller;

import dongalleto.cqrs.CQRSingredient;
import dongalleto.dao.DaoIngredient;
import dongalleto.model.Ingredient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControllerIngredient {

    static DaoIngredient dao = new DaoIngredient();
    static CQRSingredient cqrI = new CQRSingredient();
    

    public List<Ingredient> getAll() throws ClassNotFoundException, SQLException, IOException {
        return dao.getAllIngredients();
    }
    // cambio
    public Ingredient updateStock(int id, double quantity) throws SQLException, ClassNotFoundException, IOException {
        return cqrI.updateStock(id, quantity);
    }

    public Ingredient getIngredientById(int id) throws SQLException, ClassNotFoundException, IOException {
       return dao.getIngredientById(id);
    }
    public List<Ingredient> getAllRecipesAvailability(int id_recipe) throws ClassNotFoundException, SQLException, IOException {
        return dao.getIngredientsByRecipeId(id_recipe);
    }
    
    
}