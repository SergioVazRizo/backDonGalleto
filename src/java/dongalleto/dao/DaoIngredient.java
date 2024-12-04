package dongalleto.dao;

import dongalleto.bd.ConexionMySQL;
import dongalleto.model.Ingredient;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoIngredient {

    public List<Ingredient> getAllIngredients() throws ClassNotFoundException, SQLException, IOException {
        String query = "SELECT * FROM ingredients";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<Ingredient> ingredients = new ArrayList<>();
        while (rs.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getInt("id"));
            ingredient.setName(rs.getString("name"));
            ingredient.setStock(rs.getDouble("stock"));
            ingredient.setUnit(rs.getString("unit"));
            ingredient.setMinimumStock(rs.getDouble("minimum_stock"));
            ingredient.setCost(rs.getDouble("cost"));
            ingredient.setExpirationDate(rs.getString("expiration_date"));

            ingredients.add(ingredient);
        }

        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);
        return ingredients;
    }

    public Ingredient getIngredientById(int id) throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT * FROM ingredients WHERE id = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        Ingredient ingredient = null;
        if (rs.next()) {
            ingredient = new Ingredient();
            ingredient.setId(rs.getInt("id"));
            ingredient.setName(rs.getString("name"));
            ingredient.setStock(rs.getDouble("stock"));
            ingredient.setUnit(rs.getString("unit"));
            ingredient.setMinimumStock(rs.getDouble("minimum_stock"));
            ingredient.setCost(rs.getDouble("cost"));
            ingredient.setExpirationDate(rs.getString("expiration_date"));
        }

        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);
        return ingredient;
    }
    
    public Ingredient updateStock(int id, double quantity) throws SQLException, ClassNotFoundException, IOException {
        Ingredient ingredient = null;
        ConexionMySQL connMysql = new ConexionMySQL();
        
        String updateQuery = "UPDATE ingredients SET stock = stock + ? WHERE id = ?";
        
        Connection conn = connMysql.abrirConexion();

        try {
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setDouble(1, quantity); 
            updateStmt.setInt(2, id);  
            
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("No se encontró el ingrediente con ID " + id);
            }

            // Obtener el ingrediente actualizado
            String selectQuery = "SELECT * FROM ingredients WHERE id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setInt(1, id);
            
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setStock(rs.getDouble("stock"));
                ingredient.setUnit(rs.getString("unit"));
                ingredient.setMinimumStock(rs.getDouble("minimum_stock"));
                ingredient.setCost(rs.getDouble("cost"));
                ingredient.setExpirationDate(rs.getString("expiration_date"));
            }
            
            rs.close();
            selectStmt.close();

        } finally {
            connMysql.cerrarConexion(conn);
        }

        return ingredient; 
    }
    
    
    
    
    
    
    
    
    
    public List<Ingredient> getIngredientsByRecipeId(int recipeId) throws ClassNotFoundException, SQLException, IOException {
        List<Ingredient> ingredientsList = new ArrayList<>();

        // Consulta SQL para obtener los detalles de la receta desde la vista
        String query = "SELECT ingredient_ids, ingredient_quantities, ingredient_stocks, ingredient_names "
                + "FROM recipe_details WHERE recipe_id = ?;";

        // Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        // Preparar la consulta
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, recipeId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            // Obtener las cadenas concatenadas
            String ingredientIdsStr = rs.getString("ingredient_ids");
            String ingredientQuantitiesStr = rs.getString("ingredient_quantities");
            String ingredientStocksStr = rs.getString("ingredient_stocks");
            String ingredientNamesStr = rs.getString("ingredient_names");

            // Procesar los IDs de ingredientes
            if (ingredientIdsStr != null && !ingredientIdsStr.isEmpty()) {
                String[] ingredientIdsArray = ingredientIdsStr.split(",");
                String[] ingredientQuantitiesArray = ingredientQuantitiesStr.split(",");
                String[] ingredientStocksArray = ingredientStocksStr.split(",");
                String[] ingredientNamesArray = ingredientNamesStr.split(",");

                for (int i = 0; i < ingredientIdsArray.length; i++) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(Integer.parseInt(ingredientIdsArray[i].trim()));
                    ingredient.setName(ingredientNamesArray[i].trim());
                    ingredient.setStock(Double.parseDouble(ingredientStocksArray[i].trim()));
                    // Puedes agregar más propiedades si es necesario

                    ingredientsList.add(ingredient);
                }
            }
        }

        // Cerrar conexiones
        rs.close();
        pstmt.close();
        connMySQL.cerrarConexion(conn);

        return ingredientsList;
    }
    
    
    
    
}