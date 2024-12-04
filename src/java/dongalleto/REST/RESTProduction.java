package dongalleto.REST;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dongalleto.Controller.ControllerProduction;
import dongalleto.model.Production;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import com.google.gson.JsonArray;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

@Path("production")
public class RESTProduction {

    private final ControllerProduction controller = new ControllerProduction();

    @Path("status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductionStatus() throws ClassNotFoundException, SQLException, IOException {

        // Obtener todas las producciones
        List<Production> allProductions = controller.getAllProductions();

        // Agrupar las producciones por estado
        Map<String, List<Production>> groupedProductions = new HashMap<>();
        for (Production production : allProductions) {
            String status = production.getProductionStatus();
            groupedProductions.putIfAbsent(status, new ArrayList<>());
            groupedProductions.get(status).add(production);
        }

        // Crear el objeto JSON de respuesta
        JsonObject jsonResponse = new JsonObject();
        for (Map.Entry<String, List<Production>> entry : groupedProductions.entrySet()) {
            String status = entry.getKey();
            List<Production> productions = entry.getValue();

            // Crear un array JSON para las producciones de este estado
            JsonArray productionArray = new JsonArray();
            for (Production production : productions) {
                JsonObject productionJson = new JsonObject();
                productionJson.addProperty("id", production.getId());
                productionJson.addProperty("cookieId", production.getCookieId());
                productionJson.addProperty("unitsProduced", production.getUnitsProduced());
                productionArray.add(productionJson);
            }

            // Agregar el array JSON al objeto principal bajo la clave del estado
            jsonResponse.add(status, productionArray);
        }

        // Retornar la respuesta
        return Response.ok(jsonResponse.toString()).build();
    }

    @PATCH
    @Path("cookies/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProductionStatus(@PathParam("id") int cookieId, String requestBody) {
        try {
            // Parsear el JSON de entrada
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(requestBody).getAsJsonObject();
            String newStatus = jsonObject.get("newStatus").getAsString();

            // Actualizar el estado
            Production updatedProduction = controller.updateProductionStatus(cookieId, newStatus);

            if (updatedProduction != null) {
                // Preparar la respuesta
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("productionStatus", updatedProduction.getProductionStatus());
                return Response.ok(responseJson.toString()).build();
            } else {
                // Si no se encontró la producción
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("message", "Producción no encontrada");
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse.toString()).build();
            }
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error al actualizar el estado: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse.toString()).build();
        }
    }

    @POST
    @Path("cookies/{id}/complete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeCookieProduction(@PathParam("id") int cookieId) {
        try {
            Production completedProduction = controller.completeProduction(cookieId);

            if (completedProduction != null) {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("cookieId", completedProduction.getCookieId());
                responseJson.addProperty("unitsProduced", completedProduction.getUnitsProduced());
                responseJson.addProperty("newStock", completedProduction.getNewStock());
                responseJson.addProperty("status", completedProduction.getProductionStatus());

                return Response.ok(responseJson.toString()).build();
            } else {
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("message", "Producción no encontrada o no se pudo completar");
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse.toString()).build();
            }
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error al completar la producción: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse.toString()).build();
        }
    }

    @Path("cookies")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCookieToProduction(String requestBody) {
        try {
            // Parsear el cuerpo de la solicitud JSON
            JsonObject jsonRequest = new com.google.gson.JsonParser().parse(requestBody).getAsJsonObject();
            int cookieId = jsonRequest.get("cookieId").getAsInt();

            // Crear una nueva producción con estado inicial
            Production newProduction = new Production();
            newProduction.setCookieId(cookieId);
            newProduction.setProductionStatus("preparacion"); // Estado inicial
            newProduction.setUnitsProduced(0); // Unidades iniciales

            // Insertar en la base de datos y obtener el ID generado            
            int generatedId = controller.addCookieProduction(newProduction);

            // Crear respuesta JSON con el ID y estado inicial
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("productionStatus", newProduction.getProductionStatus());

            return Response.ok(jsonResponse.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Error al agregar la galleta a producción.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse.toString()).build();
        }
    }

}
