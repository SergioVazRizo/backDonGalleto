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
import dongalleto.model.Cookie;
import dongalleto.modelView.ModelViewCookie;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PathParam;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.sql.SQLException;

@Path("cookie")
public class RESTCookie {

    @Path("getCookies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCookies() throws ClassNotFoundException, SQLException, IOException {
        ControllerCookie controller = new ControllerCookie();
        List<Cookie> cookies = controller.getAll();

        // Crear el objeto JSON principal
        JsonObject jsonResponse = new JsonObject();
        JsonArray cookiesArray = new JsonArray();

        // Iterar sobre las cookies y construir la estructura
        for (Cookie cookie : cookies) {
            JsonObject cookieJson = new JsonObject();
            cookieJson.addProperty("id", cookie.getId());
            cookieJson.addProperty("name", cookie.getName());
            cookieJson.addProperty("recipeId", cookie.getRecipeId());
            cookieJson.addProperty("description", cookie.getDescription());
            cookieJson.addProperty("status", cookie.getStatus());

            JsonObject priceJson = new JsonObject();
            priceJson.addProperty("unit", cookie.getUnitPrice());
            priceJson.addProperty("package500g", cookie.getPackage500gPrice());
            priceJson.addProperty("package1000g", cookie.getPackage1000gPrice());
            priceJson.addProperty("pricePerGram", cookie.getPricePerGram());

            cookieJson.add("price", priceJson);

            cookieJson.addProperty("stock", cookie.getStock());
            cookieJson.addProperty("weightPerUnit", cookie.getWeightPerUnit());

            // Agregar la cookie al array
            cookiesArray.add(cookieJson);
        }

        // Agregar el array al objeto principal
        jsonResponse.add("cookies", cookiesArray);

        // Retornar la respuesta
        return Response.ok(jsonResponse.toString()).build();
    }

    @Path("/{id}/stock")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCookieStock(@PathParam("id") int id, String requestBody) throws ClassNotFoundException, IOException {
        int quantity;

        try {
            JsonObject jsonObject = new JsonParser().parse(requestBody).getAsJsonObject();
            quantity = jsonObject.get("quantity").getAsInt();
        } catch (Exception e) {

            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "La cantidad debe ser un número válido.");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse.toString()).build();
        }

        String out;
        ControllerCookie objCC = new ControllerCookie();

        try {

            Cookie updatedCookie = objCC.updateStock(id, quantity);

            if (updatedCookie == null) {

                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("message", "Cookie no encontrada o no se pudo actualizar.");
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse.toString()).build();
            }

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("id", updatedCookie.getId());
            responseJson.addProperty("stock", updatedCookie.getStock());
            responseJson.addProperty("status", updatedCookie.getStatus());

            return Response.ok(responseJson.toString()).build();  // Devuelve la cookie actualizada como JSON

        } catch (SQLException ex) {
            out = "{\"result\":\"Error al actualizar el stock de la cookie: " + ex.getMessage() + "\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"result\":\"" + out + "\"}").build();
        }
    }

    @Path("/{id}/status")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCookieStatus(@PathParam("id") int id, String requestBody) throws ClassNotFoundException, IOException {
        String status;

        try {
            JsonObject jsonObject = new JsonParser().parse(requestBody).getAsJsonObject();
            status = jsonObject.get("status").getAsString();  // Obtener el valor de 'status'
        } catch (Exception e) {

            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "El estado debe ser 'Existencia' o 'Agotado'.");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse.toString()).build();
        }

        ControllerCookie objCC = new ControllerCookie();
        try {
            // Llamamos al controlador para actualizar el estado de la cookie
            Cookie updatedCookie = objCC.updateStatus(id, status);

            if (updatedCookie == null) {
                // Si no se encuentra la cookie o no se pudo actualizar, devolvemos un error 404
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("message", "Cookie no encontrada o no se pudo actualizar.");
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse.toString()).build();
            }

            // Si la cookie se actualizó correctamente, devolvemos los datos de la cookie actualizada
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("id", updatedCookie.getId());
            responseJson.addProperty("status", updatedCookie.getStatus());

            return Response.ok(responseJson.toString()).build();  // Respuesta exitosa con la cookie actualizada

        } catch (SQLException ex) {
            // Si ocurre un error en la base de datos, devolvemos un error 500
            String out = "{\"result\":\"Error al actualizar el estado de la cookie: " + ex.getMessage() + "\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"result\":\"" + out + "\"}").build();
        }
    }

    @Path("getCookiesPublic")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCookiesPublic() throws SQLException {
        String out = "";
        ControllerCookie cookies = new ControllerCookie();
        List<ModelViewCookie> listaLibros = null;
        listaLibros = cookies.getAllPublic();
        Gson objGson = new Gson();
        out = objGson.toJson(listaLibros);
        return Response.ok(out).build();

    }

    @Path("getCookiesPublicTodos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCookiesPublicAll() throws SQLException, ClassNotFoundException {
        String out = "";
        try {
            ControllerCookie cookies = new ControllerCookie();
            List<ModelViewCookie> listaLibros = null;
            listaLibros = cookies.getCookiesAll();
            Gson objGson = new Gson();
            out = objGson.toJson(listaLibros);
            return Response.ok(out).build();
        } catch (IOException ex) {
            Logger.getLogger(RESTCookie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok(out).build();
    }

}
