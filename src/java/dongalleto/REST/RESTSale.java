package dongalleto.REST;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dongalleto.Controller.ControllerSale;
import dongalleto.model.Sale;
import dongalleto.model.SaleItem;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("sale")
public class RESTSale {

    @GET
    @Path("getAllSales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSales() {
        ControllerSale controller = new ControllerSale();
        Gson gson = new Gson();
        try {
            // Llamar al método del controlador para obtener todas las ventas
            List<Sale> sales = controller.getAll();

            // Crear el objeto JSON principal
            JsonObject jsonResponse = new JsonObject();
            JsonArray salesArray = new JsonArray();

            // Iterar sobre las ventas y construir la estructura JSON
            for (Sale sale : sales) {
                JsonObject saleJson = new JsonObject();
                saleJson.addProperty("id", sale.getId());
                saleJson.addProperty("date", sale.getDate().toString());
                saleJson.addProperty("total", sale.getTotal());

                // Crear el arreglo de ítems para la venta actual
                JsonArray itemsArray = new JsonArray();
                for (SaleItem item : sale.getItems()) {
                    JsonObject itemJson = new JsonObject();
                    itemJson.addProperty("cookieId", item.getCookieId());
                    itemJson.addProperty("cookieName", item.getCookieName());
                    itemJson.addProperty("quantity", item.getQuantity());
                    itemJson.addProperty("pricePerUnit", item.getPricePerUnit());
                    itemJson.addProperty("subtotal", item.getSubtotal());
                    itemsArray.add(itemJson);
                }

                saleJson.add("items", itemsArray);

                // Agregar la venta al array principal
                salesArray.add(saleJson);
            }

            // Agregar el array de ventas al objeto JSON principal
            jsonResponse.add("sales", salesArray);

            // Retornar la respuesta como JSON
            return Response.ok(jsonResponse.toString()).build();

        } catch (ClassNotFoundException | SQLException | IOException e) {
            // En caso de error, devolver una respuesta con estado 500
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error al obtener las ventas: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse.toString()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)  // Responde en formato JSON
    @Consumes(MediaType.APPLICATION_JSON)  // Recibe los datos en formato JSON
    @Path("createSale")
    public Response createSale(String jsonInput) {
        ControllerSale controller = new ControllerSale();
        try {
            // Usamos el método parse() en lugar de parseString() para versiones anteriores de Gson
            JsonParser parser = new JsonParser();  // Instanciamos JsonParser
            JsonObject saleJson = parser.parse(jsonInput).getAsJsonObject();  // Usamos parse()

            // Extraemos el total y la fecha
            double total = saleJson.get("total").getAsDouble();
            String date = saleJson.has("date") ? saleJson.get("date").getAsString() : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());

            // Crear el objeto Sale con los datos de la venta
            Sale sale = new Sale();
            sale.setTotal(total);
            sale.setDate(date);

            // Crear los ítems de la venta
            JsonArray itemsArray = saleJson.getAsJsonArray("items");
            List<SaleItem> saleItems = new ArrayList<>();
            for (JsonElement itemElement : itemsArray) {
                JsonObject itemJson = itemElement.getAsJsonObject();

                SaleItem saleItem = new SaleItem();
                saleItem.setCookieId(itemJson.get("cookieId").getAsInt());
                saleItem.setCookieName(itemJson.get("cookieName").getAsString());
                saleItem.setQuantity(itemJson.get("quantity").getAsInt());
                saleItem.setPricePerUnit(itemJson.get("pricePerUnit").getAsDouble());
                saleItem.setSubtotal(itemJson.get("subtotal").getAsDouble());
                saleItem.setSaleType(itemJson.get("saleType").getAsString());

                saleItems.add(saleItem);
            }
            sale.setItems(saleItems);

            // Llamamos al controlador para crear la venta en la base de datos
            Sale createdSale = controller.createSale(sale);

            // Crear el JSON para la respuesta
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("id", createdSale.getId());  // ID de la venta
            responseJson.addProperty("date", createdSale.getDate());  // Fecha de la venta
            responseJson.addProperty("total", createdSale.getTotal());  // Total de la venta

            // Crear el arreglo de ítems para la respuesta
            JsonArray itemsArrayResponse = new JsonArray();
            for (SaleItem item : createdSale.getItems()) {
                JsonObject itemJson = new JsonObject();
                itemJson.addProperty("cookieId", item.getCookieId());
                itemJson.addProperty("cookieName", item.getCookieName());  // Este campo debe ser obtenido de la base de datos si lo necesitas
                itemJson.addProperty("quantity", item.getQuantity());
                itemJson.addProperty("pricePerUnit", item.getPricePerUnit());  // Este campo puede ser parte de SaleItem, según la implementación
                itemJson.addProperty("subtotal", item.getSubtotal());
                itemJson.addProperty("saleType", item.getSaleType());
                itemsArrayResponse.add(itemJson);
            }
            responseJson.add("items", itemsArrayResponse);  // Agregar los ítems a la respuesta

            // Retornar la respuesta con el JSON completo
            return Response.ok(responseJson.toString()).build();

        } catch (SQLException | ClassNotFoundException | IOException | JsonSyntaxException e) {
            // En caso de error, devolvemos un mensaje de error
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error al procesar la venta: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse.toString()).build();
        }
    }

    @GET
    @Path("validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateSale(@QueryParam("cookieId") int cookieId,
            @QueryParam("quantity") int quantity,
            @QueryParam("saleType") String saleType) {
        ControllerSale controller = new ControllerSale();
        try {
            // Validar la venta utilizando el controlador
            JsonObject validationResult = controller.validateSale(cookieId, quantity, saleType);

            // La respuesta de validación es un JSON que contiene si es válida, el mensaje, cantidad actual y total
            return Response.ok(validationResult.toString()).build();

        } catch (Exception e) {
            // En caso de error, retornamos un mensaje de error
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error al validar la venta: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse.toString()).build();
        }
    }

}