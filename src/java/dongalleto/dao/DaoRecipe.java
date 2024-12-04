/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dongalleto.dao;

import dongalleto.bd.ConexionMySQL;
import dongalleto.model.Recipes;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aaron
 */
public class DaoRecipe {

    public List<Recipes> getAllRecipe(int id) throws ClassNotFoundException, SQLException, IOException {
        // Consulta SQL para obtener todos los registros de la vista recipe_details
        String query = "SELECT * FROM recipe_details WHERE recipe_id = ?;";  // Puedes especificar un ID o dejar el WHERE abierto

        // Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        // Preparar la consulta
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);  // Por ejemplo, filtrar por recipe_id = 1
        ResultSet rs = pstmt.executeQuery();

        // Lista para almacenar las recetas
        List<Recipes> recipes = new ArrayList<>();
        Recipes currentRecipe = null;

        // Iterar sobre los resultados
        while (rs.next()) {
            int recipeId = rs.getInt("recipe_id");

            // Si la receta no está en la lista, crearla
            if (currentRecipe == null || currentRecipe.getRecipe_id() != recipeId) {
                currentRecipe = new Recipes();
                currentRecipe.setRecipe_id(recipeId);
                currentRecipe.setRecipe_name(rs.getString("recipe_name"));
                currentRecipe.setRecipe_yield(rs.getInt("recipe_yield"));

                // Convertir instrucciones de texto a lista
                String instructions = rs.getString("recipe_instructions");
                if (instructions != null) {
                    currentRecipe.setInstructions(Arrays.asList(instructions.split("\n")));
                } else {
                    currentRecipe.setInstructions(new ArrayList<>());
                }

                // Añadir la receta a la lista
                recipes.add(currentRecipe);
            }

            // Manejar ingredient_ids como cadena separada por comas
            String ingredientIdsStr = rs.getString("ingredient_ids");
            List<Integer> ingredientIds = currentRecipe.getIngredient_ids() != null ? currentRecipe.getIngredient_ids() : new ArrayList<>();
            if (ingredientIdsStr != null && !ingredientIdsStr.isEmpty()) {
                String[] ingredientIdsArray = ingredientIdsStr.split(",");
                for (String ingredientId : ingredientIdsArray) {
                    ingredientIds.add(Integer.parseInt(ingredientId.trim()));  // Añadir cada ID a la lista
                }
            }

            // Manejar las demás columnas (ahora dividiendo y convirtiendo los valores)
            List<Double> ingredientQuantities = currentRecipe.getIngredient_quantities() != null ? currentRecipe.getIngredient_quantities() : new ArrayList<>();
            String ingredientQuantitiesStr = rs.getString("ingredient_quantities");
            if (ingredientQuantitiesStr != null && !ingredientQuantitiesStr.isEmpty()) {
                String[] ingredientQuantitiesArray = ingredientQuantitiesStr.split(",");
                for (String quantity : ingredientQuantitiesArray) {
                    ingredientQuantities.add(Double.parseDouble(quantity.trim()));  // Convertir a Double
                }
            }

            List<String> ingredientNames = currentRecipe.getIngredient_names() != null ? currentRecipe.getIngredient_names() : new ArrayList<>();
            String ingredientNamesStr = rs.getString("ingredient_names");
            if (ingredientNamesStr != null && !ingredientNamesStr.isEmpty()) {
                String[] ingredientNamesArray = ingredientNamesStr.split(",");
                ingredientNames.addAll(Arrays.asList(ingredientNamesArray));
            }

            List<Double> ingredientStocks = currentRecipe.getIngredient_stocks() != null ? currentRecipe.getIngredient_stocks() : new ArrayList<>();
            String ingredientStocksStr = rs.getString("ingredient_stocks");
            if (ingredientStocksStr != null && !ingredientStocksStr.isEmpty()) {
                String[] ingredientStocksArray = ingredientStocksStr.split(",");
                for (String stock : ingredientStocksArray) {
                    ingredientStocks.add(Double.parseDouble(stock.trim()));  // Convertir a Double
                }
            }

            List<String> ingredientUnits = currentRecipe.getIngredient_units() != null ? currentRecipe.getIngredient_units() : new ArrayList<>();
            String ingredientUnitsStr = rs.getString("ingredient_units");
            if (ingredientUnitsStr != null && !ingredientUnitsStr.isEmpty()) {
                String[] ingredientUnitsArray = ingredientUnitsStr.split(",");
                ingredientUnits.addAll(Arrays.asList(ingredientUnitsArray));
            }

            // Asignar las listas a la receta
            currentRecipe.setIngredient_ids(ingredientIds);
            currentRecipe.setIngredient_quantities(ingredientQuantities);
            currentRecipe.setIngredient_names(ingredientNames);
            currentRecipe.setIngredient_stocks(ingredientStocks);
            currentRecipe.setIngredient_units(ingredientUnits);
        }

        // Cerrar conexiones
        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);

        return recipes;
    }

    
    
}