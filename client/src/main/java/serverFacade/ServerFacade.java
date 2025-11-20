package serverFacade;

import com.google.gson.Gson;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private HttpClient client = HttpClient.newHttpClient();
    private String serverUrl;
    private Gson serializer;

    private String authToken;

    public ServerFacade (String serverUrl){
        this.serverUrl = serverUrl;
        serializer = new Gson();
    }

    public void register(RegisterRequest registerRequest){
        HttpRequest request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
    }

    public void login(LoginRequest loginRequest){
        HttpRequest request = buildRequest("POST", "/session", loginRequest);
        model.LoginResult loginResult = serializer.fromJson(sendRequest(request).body().toString(), LoginResult.class);

        authToken =loginResult.authToken();
    }

    public void logout(LogoutRequest logoutRequest){
        LogoutRequest logoutRequestAuthorized = new LogoutRequest(authToken);
        HttpRequest request = buildRequest("DELETE", "/session", logoutRequestAuthorized);
        var response = sendRequest(request);
    }

    public void listGames(ListGamesRequest listGamesRequest){
        ListGamesRequest listGamesRequestAuthorized = new ListGamesRequest(authToken);
        HttpRequest request = buildRequest("GET", "/game", listGamesRequestAuthorized);
        var response = sendRequest(request);
    }

    public void createGame(CreateGameRequest createGameRequest){
        HttpRequest request = buildRequest("POST", "/game", createGameRequest);
        var response = sendRequest(request);
    }

    public void joinGame(JoinGameRequest joinGameRequest){
        HttpRequest request = buildRequest("PUT", "/game", joinGameRequest);
        var response = sendRequest(request);
    }

    private HttpRequest buildRequest(String method, String path, Object body){
        var request = HttpRequest.newBuilder().uri(URI.create(serverUrl + path)).setHeader("Content-Type", "application/json");
        if (body != null) {
            String jsonBody = serializer.toJson(body);
            request.method(method, HttpRequest.BodyPublishers.ofString(jsonBody));
        }
        else {
            request.method(method, HttpRequest.BodyPublishers.noBody());
        }
        return request.build();
    }

    private HttpResponse sendRequest(HttpRequest request){
        HttpResponse response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e){
            handleErrors(e.getMessage());
        }
        if (response == null){
            handleErrors("Server Error");
        } else if (!validResponse(response.statusCode())){
            handleErrors("Server Status Error" + response.statusCode() + response.body());
        }

        return response;
    }

    private void handleErrors(String error){
        System.out.println(error);
    }

    private boolean validResponse(int serverStatus){
        return serverStatus / 100 == 2;
    }

}
