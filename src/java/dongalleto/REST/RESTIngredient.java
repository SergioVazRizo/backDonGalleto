package dongalleto.REST;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dongalleto.Controller.ControllerIngredient;
import dongalleto.model.Ingredient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PathParam;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Path("ingredients")
public class RESTIngredient {

    private final ControllerIngredient controller = new ControllerIngredient();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIngredients() throws ClassNotFoundException, SQLException, IOException {
        List<Ingredient> ingredients = controller.getAll();
        
        JsonObject jsonResponse = new JsonObject();
        JsonArray ingredientsArray = new JsonArray();

        for (Ingredient ingredient : ingredients) {
            JsonObject ingredientJson = new JsonObject();
            ingredientJson.addProperty("id", ingredient.getId());
            ingredientJson.addProperty("name", ingredient.getName());
            ingredientJson.addProperty("stock", ingredient.getStock());
            ingredientJson.addProperty("unit", ingredient.getUnit());
            ingredientJson.addProperty("minimumStock", ingredient.getMinimumStock());
            ingredientJson.addProperty("cost", ingredient.getCost());

            ingredientsArray.add(ingredientJson);
        }

        jsonResponse.add("ingredients", ingredientsArray);
        
        return Response.ok(jsonResponse.toString()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIngredientById(@PathParam("id") int id) throws ClassNotFoundException, SQLException, IOException {
        Ingredient ingredient = controller.getIngredientById(id);

        if (ingredient == null) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Ingrediente no encontrado.");
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse.toString()).build();
        }

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("id", ingredient.getId());
        responseJson.addProperty("name", ingredient.getName());
        responseJson.addProperty("stock", ingredient.getStock());
        responseJson.addProperty("unit", ingredient.getUnit());
        responseJson.addProperty("minimumStock", ingredient.getMinimumStock());
        responseJson.addProperty("cost", ingredient.getCost());

        return Response.ok(responseJson.toString()).build();
    }

    @Path("/{id}/stock")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateIngredientStock(@PathParam("id") int id, String requestBody) throws ClassNotFoundException, IOException {

       double quantity;

       try {
           JsonParser parser = new JsonParser();
           JsonObject jsonObject = parser.parse(requestBody).getAsJsonObject();
           quantity = jsonObject.get("quantity").getAsDouble(); 
       } catch (Exception e) {
           JsonObject errorResponse = new JsonObject();
           errorResponse.addProperty("message", "La cantidad debe ser un número válido.");
           return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse.toString()).build();
       }

       try {
           Ingredient updatedIngredient = controller.updateStock(id, quantity);

           if (updatedIngredient == null) {
               JsonObject errorResponse = new JsonObject();
               errorResponse.addProperty("message", "Ingrediente no encontrado o no se pudo actualizar.");
               return Response.status(Response.Status.NOT_FOUND).entity(errorResponse.toString()).build();
           }

           JsonObject responseJson = new JsonObject();
           responseJson.addProperty("id", updatedIngredient.getId());
           responseJson.addProperty("stock", updatedIngredient.getStock());
           responseJson.addProperty("belowMinimum", updatedIngredient.getStock() < updatedIngredient.getMinimumStock());

           return Response.ok(responseJson.toString()).build();  

       } catch (SQLException ex) {
           String out = "{\"result\":\"Error al actualizar el stock del ingrediente: " + ex.getMessage() + "\"}";
           return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"result\":\"" + out + "\"}").build();
       }
   }
}