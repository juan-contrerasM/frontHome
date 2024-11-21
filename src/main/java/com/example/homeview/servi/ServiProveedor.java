package com.example.homeview.servi;

import com.example.homeview.model.Proveedor;
import com.example.homeview.model.Pyp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ServiProveedor {

    private static final String BASE_URL = "http://localhost:8080/proveedor/";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Proveedor> obtenerProveedores() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL+"obtenerProveedores"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Imprime la respuesta para depuración
        System.out.println("JSON recibido: " + response.body());

        // Deserializa el JSON a una lista de objetos Pyp
        return gson.fromJson(response.body(), new TypeToken<List<Proveedor>>() {}.getType());
    }
    public Proveedor obtenerProveedor(String cedula) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL+"obtenerProveedor/"+cedula))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Imprime la respuesta para depuración
        System.out.println("JSON recibido: " + response.body());

        // Deserializa el JSON a un objeto Pyp
        return gson.fromJson(response.body(), Proveedor.class);
    }


    // Método para guardar un Pyp (usando JSON en el cuerpo del request)
    public void guardarProveedor(String jsonPyp) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "guardarProveedor"))
                .header("Content-Type", "application/json") // Indica que el cuerpo es un JSON
                .POST(HttpRequest.BodyPublishers.ofString(jsonPyp)) // Cuerpo del request en formato String (JSON)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de la respuesta
        if (response.statusCode() == 200) {
            System.out.println("proveedor guardado correctamente");
        } else {
            System.out.println("Error al guardar el proveedor: " + response.statusCode());
            System.out.println(response.body());
        }
    }
    // Método para actualizar un Pyp (usando JSON en el cuerpo del request)
    public void actualizarproveedor(String jsonPyp,String  cedula) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "actualizarProveedor/"+cedula)) // Endpoint para actualizar
                .header("Content-Type", "application/json") // Indica que el cuerpo es un JSON
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPyp)) // Cuerpo del request en formato String (JSON)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de la respuesta
        if (response.statusCode() == 200) {
            System.out.println("Proveedor actualizado correctamente");
        } else {
            System.out.println("Error al actualizar el Proveedor: " + response.statusCode());
            System.out.println(response.body());
        }
    }
    // Método para eliminar un Pyp por su ID
    public void eliminarProveedor(String  cedula) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "eliminarProveedor/" + cedula)) // Endpoint para eliminar con el ID en la URL
                .DELETE() // Método HTTP DELETE
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de la respuesta
        if (response.statusCode() == 200) {
            System.out.println("Proveedor eliminado correctamente");
        } else {
            System.out.println("Error al eliminar el Proveedor: " + response.statusCode());
            System.out.println(response.body());
        }
    }

}
