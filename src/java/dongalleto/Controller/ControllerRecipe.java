/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dongalleto.Controller;
import dongalleto.dao.DaoRecipe;
import dongalleto.model.Recipes;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class ControllerRecipe {
    private static DaoRecipe d = new DaoRecipe();
    
     public List<Recipes> getAllRecipes(int id) throws ClassNotFoundException, SQLException, IOException {
        return d.getAllRecipe(id);
    }
     
}