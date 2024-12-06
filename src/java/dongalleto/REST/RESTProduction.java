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

        List<Production> allProductions = controller.getAllProductions();

        Map<String, List<Production>> groupedProductions = new HashMap<>();

        for (Production production : allProductions) {
            String status = production.getProductionStatus();
            groupedProductions.putIfAbsent(status, new ArrayList<>());
            groupedProductions.get(status).add(production);
        }

        JsonObject jsonResponse = new JsonObject();
        for (Map.Entry<String, List<Production>> entry : groupedProductions.entrySet()) {
            String status = entry.getKey();
            List<Production> productions = entry.getValue();

            JsonArray productionArray = new JsonArray();
            for (Production production : productions) {
                JsonObject productionJson = new JsonObject();
                productionJson.addProperty("id", production.getId());
                productionJson.addProperty("cookieId", production.getCookieId());
                productionJson.addProperty("unitsProduced", production.getUnitsProduced());
                productionArray.add(productionJson);
            }

            jsonResponse.add(status, productionArray);
        }

        return Response.ok(jsonResponse.toString()).build();
    }

    @PATCH
    @Path("cookies/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProductionStatus(@PathParam("id") int productionId, String requestBody) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(requestBody).getAsJsonObject();
            String newStatus = jsonObject.get("newStatus").getAsString();

            Production updatedProduction = controller.updateProductionStatus(productionId, newStatus);

            if (updatedProduction != null) {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("productionStatus", updatedProduction.getProductionStatus());
                return Response.ok(responseJson.toString()).build();
            } else {
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

            JsonObject jsonRequest = new com.google.gson.JsonParser().parse(requestBody).getAsJsonObject();
            int cookieId = jsonRequest.get("cookieId").getAsInt();

            Production newProduction = new Production();
            newProduction.setCookieId(cookieId);
            newProduction.setProductionStatus("preparacion");
            newProduction.setUnitsProduced(0);

            int generatedId = controller.addCookieProduction(newProduction);

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
