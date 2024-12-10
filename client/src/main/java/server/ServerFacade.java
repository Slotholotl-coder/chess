package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServerFacade {
    private final String baseUrl;
    private final HttpClient client;
    private String authToken;

    public ServerFacade(String url) {
        this.baseUrl = url;
        this.client = HttpClient.newHttpClient();
    }

    public boolean register(String username, String password, String email) throws Exception {
        String path = "/user";
        Map<String, String> body = Map.of("username", username, "password", password, "email", email);
        HttpResponse<String> response = sendRequest("POST", path, body, null);

        if (response.statusCode() == 200) {
            Map<String, String> responseBody = new Gson().fromJson(response.body(), Map.class);
            authToken = responseBody.get("authToken");
            return true;
        }
        return false;
    }

    public boolean login(String username, String password) throws Exception {
        String path = "/session";
        Map<String, String> body = Map.of("username", username, "password", password);
        HttpResponse<String> response = sendRequest("POST", path, body, null);

        if (response.statusCode() == 200) {
            Map<String, String> responseBody = new Gson().fromJson(response.body(), Map.class);
            authToken = responseBody.get("authToken");
            return true;
        }
        return false;
    }

    public boolean logout() throws Exception {
        String path = "/session";
        HttpResponse<String> response = sendRequest("DELETE", path, null, authToken);

        if (response.statusCode() == 200) {
            authToken = null;
            return true;
        }
        return false;
    }

    public int createGame(String gameName) throws Exception {
        String path = "/game";
        Map<String, String> body = Map.of("gameName", gameName);
        HttpResponse<String> response = sendRequest("POST", path, body, authToken);

        if (response.statusCode() == 200) {
            Map<String, Double> responseBody = new Gson().fromJson(response.body(), Map.class);
            return responseBody.get("gameID").intValue();
        }
        throw new Exception("Failed to create game");
    }

    public String listGames() throws Exception {
        String path = "/game";
        HttpResponse<String> response = sendRequest("GET", path, null, authToken);

        if (response.statusCode() == 200) {
            return response.body();
        }
        throw new Exception("Failed to list games");
    }

    public boolean joinGame(int gameId, String playerColor) throws Exception {
        String path = "/game";
        Map<String, Object> body = playerColor != null ?
                Map.of("gameID", gameId, "playerColor", playerColor) :
                Map.of("gameID", gameId);
        HttpResponse<String> response = sendRequest("PUT", path, body, authToken);

        return response.statusCode() == 200;
    }

    private HttpResponse<String> sendRequest(String method, String path, Object body, String token) throws Exception {
        String url = baseUrl + path;
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json");

        if (token != null) {
            requestBuilder.header("Authorization", token);
        }

        if (body != null) {
            String jsonBody = new Gson().toJson(body);
            requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(jsonBody));
        } else {
            requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        CompletableFuture<HttpResponse<String>> future = client.sendAsync(
                requestBuilder.build(),
                HttpResponse.BodyHandlers.ofString()
        );

        return future.get();
    }
}