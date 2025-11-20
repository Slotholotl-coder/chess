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

    public ServerFacade (String serverUrl){
        this.serverUrl = serverUrl;
        serializer = new Gson();
    }

    public void register(RegisterRequest registerRequest){
        HttpRequest request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
    }

//    public LoginResult login(LoginRequest loginRequest){}
//
//    public void logout(LogoutRequest logoutRequest){
//
//    }
//
//    public ListGamesResult listGames(ListGamesRequest listGamesRequest){
//
//    }
//
//    public CreateGameResult createGame(CreateGameRequest createGameRequest){
//
//    }
//
//    public void joinGame(JoinGameRequest joinGameRequest){
//
//    }

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
            System.out.println(e.getMessage());
        }
        return response;
    }

}
