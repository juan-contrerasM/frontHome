package com.example.homeview.servi;

import com.example.homeview.model.Operador;
import com.example.homeview.model.Proveedor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ServiOperador {

    private static final String BASE_URL = "http://localhost:8080/operador/";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = getGson();


    public List<Operador> obtenerOperadores() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "obtenerOperadores"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Imprime la respuesta para depuración
        System.out.println("JSON recibido: " + response.body());

        // Deserializa el JSON a una lista de objetos operador
        return gson.fromJson(response.body(), new TypeToken<List<Operador>>() {
        }.getType());
    }

    public Operador obtenerOperador(String cedula) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "obtenerOperador/" + cedula))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Imprime la respuesta para depuración
        System.out.println("JSON recibido: " + response.body());

        // Deserializa el JSON a un objeto Pyp
        return gson.fromJson(response.body(), Operador.class);
    }


    // Método para guardar un operador (usando JSON en el cuerpo del request)
    public void guardarOperador(String jsonPyp) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "guardarOperador"))
                .header("Content-Type", "application/json") // Indica que el cuerpo es un JSON
                .POST(HttpRequest.BodyPublishers.ofString(jsonPyp)) // Cuerpo del request en formato String (JSON)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de la respuesta
        if (response.statusCode() == 200) {
            System.out.println("operador guardado correctamente");
        } else {
            System.out.println("Error al guardar el operador: " + response.statusCode());
            System.out.println(response.body());
        }
    }

    // Método para actualizar un Pyp (usando JSON en el cuerpo del request)
    public void actualizarOperador(String jsonPyp, String cedula) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "actualizarOperador/" + cedula)) // Endpoint para actualizar
                .header("Content-Type", "application/json") // Indica que el cuerpo es un JSON
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPyp)) // Cuerpo del request en formato String (JSON)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de la respuesta
        if (response.statusCode() == 200) {
            System.out.println("operador actualizado correctamente");
        } else {
            System.out.println("Error al actualizar el operador: " + response.statusCode());
            System.out.println(response.body());
        }
    }

    // Método para eliminar un operador por su ID
    public void eliminarOperador(String cedula) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "eliminarOperador/" + cedula)) // Endpoint para eliminar con el ID en la URL
                .DELETE() // Método HTTP DELETE
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de la respuesta
        if (response.statusCode() == 200) {
            System.out.println("operador eliminado correctamente");
        } else {
            System.out.println("Error al eliminar el operador: " + response.statusCode());
            System.out.println(response.body());
        }
    }

        public static Gson getGson() {
            return new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd") // Ajusta este formato según tus necesidades
                    .create();
        }
}
