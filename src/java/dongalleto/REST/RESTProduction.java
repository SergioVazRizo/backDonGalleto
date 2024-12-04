package dongalleto.REST;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dongalleto.controller.ControllerProduction;
import dongalleto.model.Production;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;


@Path("production")
public class RESTProduction {
    private final ControllerProduction controller = new ControllerProduction();

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
}