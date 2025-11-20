package serverFacade;

import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private HttpClient client = HttpClient.newHttpClient();
    private String serverUrl;

    public ServerFacade (String serverUrl){
        this.serverUrl = serverUrl;
    }

    public RegisterResult register(RegisterRequest registerRequest){
        HttpResponse<String> request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
    }

    public LoginResult login(LoginRequest loginRequest){}

    public void logout(LogoutRequest logoutRequest){

    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest){

    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest){

    }

    public void joinGame(JoinGameRequest joinGameRequest){

    }

    private HttpRequest buildRequest(String method, String path, Object body){
        var request = HttpRequest.newBuilder().uri(URI.create(serverUrl + path)).method(method, body);
        if (body != null) {
        }
    }


}
