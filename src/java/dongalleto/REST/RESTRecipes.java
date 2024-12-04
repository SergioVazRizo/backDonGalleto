/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dongalleto.REST;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dongalleto.Controller.ControllerCookie;
import dongalleto.Controller.ControllerIngredient;
import dongalleto.Controller.ControllerRecipe;
import dongalleto.model.Cookie;
import dongalleto.model.Ingredient;
import dongalleto.model.Recipes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PathParam;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Usuario
 */
@Path("recipes")
public class RESTRecipes {

    @Path("getRecipes/{recipe_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipes(@PathParam("recipe_id") int recipeId) throws ClassNotFoundException, SQLException, IOException {
        ControllerRecipe controller = new ControllerRecipe();
        List<Recipes> recipesList = controller.getAllRecipes(recipeId);

        // Crear el objeto JSON principal
        JsonObject jsonResponse = new JsonObject();
        JsonArray recipesArray = new JsonArray();

        // Iterar sobre las recetas y construir la estructura
        for (Recipes recipe : recipesList) {
            JsonObject recipeJson = new JsonObject();
            recipeJson.addProperty("id", recipe.getRecipe_id());
            recipeJson.addProperty("name", recipe.getRecipe_name());

            // Crear el array de ingredientes
            JsonArray ingredientsArray = new JsonArray();
            for (int i = 0; i < recipe.getIngredient_ids().size(); i++) {
                JsonObject ingredientJson = new JsonObject();
                ingredientJson.addProperty("ingredientId", recipe.getIngredient_ids().get(i));
                ingredientJson.addProperty("quantity", recipe.getIngredient_quantities().get(i));
                ingredientsArray.add(ingredientJson);
            }

            // Agregar los ingredientes a la receta
            recipeJson.add("ingredients", ingredientsArray);

            // Agregar las instrucciones como un array
            JsonArray instructionsArray = new JsonArray();
            for (String instruction : recipe.getInstructions()) {
                instructionsArray.add(instruction);
            }
            recipeJson.addProperty("yield", recipe.getRecipe_yield());
            recipeJson.add("instructions", instructionsArray);

            // Agregar la receta al array de recetas
            recipesArray.add(recipeJson);
        }

        // Agregar el array de recetas al objeto principal
        jsonResponse.add("recipes", recipesArray);

        // Retornar la respuesta
        return Response.ok(jsonResponse.toString()).build();
    }

    @Path("availability/{recipe_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkRecipeAvailability(@PathParam("recipe_id") int recipeId) {
        try {
            ControllerIngredient ci = new ControllerIngredient();

            // Obtener ingredientes de la receta usando la vista
            List<Ingredient> ingredients = ci.getAllRecipesAvailability(recipeId);

            JsonObject jsonResponse = new JsonObject();
            JsonArray missingIngredientsArray = new JsonArray();

            boolean allAvailable = true;

            for (Ingredient ingredient : ingredients) {
                double requiredQuantity =100;  // Aquí debes obtener o definir cómo obtienes esta cantidad requerida
            double availableQuantity = ingredient.getStock();

                if (availableQuantity < requiredQuantity) {
                    allAvailable = false;
                    JsonObject missingIngredientJson = new JsonObject();
                    missingIngredientJson.addProperty("ingredientId", ingredient.getId());
                    missingIngredientJson.addProperty("name", ingredient.getName());
                    missingIngredientJson.addProperty("required", requiredQuantity);
                    missingIngredientJson.addProperty("available", availableQuantity);
                    missingIngredientsArray.add(missingIngredientJson);
                }
            }

            jsonResponse.addProperty("available", allAvailable);
            jsonResponse.add("missingIngredients", missingIngredientsArray);

            return Response.ok(jsonResponse.toString()).build();

        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Internal server error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse.toString()).build();
        }
    }
}