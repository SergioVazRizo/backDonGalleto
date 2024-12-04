package dongalleto.appservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dongalleto.modelView.ModelViewCookie;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CookieExternoAppService {

    /*private static final List<String> EXTERNAL_API_URLS = Arrays.asList(
        "http://10.16.1.74:8080/SistemaGestionArq/api/libro/getAllLibrosPublic", // Sergio
        "http://10.16.20.5:8080/biblioteca/api/libro/getAllLibrosPublico", // Cortez
        "http://10.16.18.48:8080/UniversidadIbero/api/libro/getAllLibrosPublico", // Hector
        "http://10.16.20.101:3000/api/book/publicBooks" //alexis
    );
     */
    List<String> url;

    // Method to fetch books from all external URLs
    public List<ModelViewCookie> getAllLibrosExtern() {
        List<ModelViewCookie> allExternalBooks = new ArrayList<>();

        for (String apiUrl : url) {
            List<ModelViewCookie> cokkieFromApi = fetchCookieFromApi(apiUrl);
            allExternalBooks.addAll(cokkieFromApi);
        }

        return allExternalBooks;
    }

    // Helper method to fetch books from a single API URL
    private List<ModelViewCookie> fetchCookieFromApi(String apiUrl) {
        List<ModelViewCookie> externalCookies = new ArrayList<>();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                List<ModelViewCookie> libros = gson.fromJson(response.toString(), new TypeToken<List<ModelViewCookie>>() {
                }.getType());
                externalCookies.addAll(libros);
            } else {
                System.out.println("GET request failed for URL: " + apiUrl + " with response code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return externalCookies;
    }

}