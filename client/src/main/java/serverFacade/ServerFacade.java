package serverFacade;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import ui.GameUI;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class ServerFacade {
    private HttpClient client = HttpClient.newHttpClient();
    private String serverUrl;
    private Gson serializer;

    private String authToken;

    private WebsocketClient websocketClient;

    private HashMap<Integer, GameData> displayedGameList;

    private GameUI gameUI;

    public ServerFacade (int port){
        this.serverUrl = "http://localhost:" + port;
        serializer = new Gson();
        displayedGameList = new HashMap<>();
    }

    public void register(RegisterRequest registerRequest) throws Exception {
        HttpRequest request = buildRequest("POST", "/user", registerRequest);
        RegisterResult response = serializer.fromJson(sendRequest(request).body().toString(), RegisterResult.class);

        authToken = response.authToken();

        login(new LoginRequest(registerRequest.username(), registerRequest.password()));
    }

    public void login(LoginRequest loginRequest) throws Exception {
        HttpRequest request = buildRequest("POST", "/session", loginRequest);
        model.LoginResult loginResult = serializer.fromJson(sendRequest(request).body().toString(), LoginResult.class);

        authToken = loginResult.authToken();
    }

    public void logout() throws Exception {
        LogoutRequest logoutRequestAuthorized = new LogoutRequest(authToken);
        HttpRequest request = buildRequest("DELETE", "/session", logoutRequestAuthorized);
        var response = sendRequest(request);

        authToken = null;
    }

    public ListGamesResult listGames() throws Exception {
        ListGamesRequest listGamesRequestAuthorized = new ListGamesRequest(authToken);
        HttpRequest request = buildRequest("GET", "/game", listGamesRequestAuthorized);
        ListGamesResult response = serializer.fromJson(sendRequest(request).body().toString(), ListGamesResult.class);

        displayGameList(response);

        return response;
    }

    private void displayGameList(ListGamesResult listGamesResult){
        int x = 0;
        displayedGameList.clear();
        for (GameData chessGame : listGamesResult.games()){
            x++;
            displayedGameList.put(x, chessGame);
            System.out.println(x + " : " + chessGame.toString());
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws Exception {
        if (authToken == null){
            handleErrors("Invalid authorization");
        }
        HttpRequest request = buildRequest("POST", "/game", createGameRequest);
        var response = sendRequest(request);
        return serializer.fromJson(response.body().toString(), CreateGameResult.class);
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws Exception {
        int displayedID = displayedGameList.get(joinGameRequest.gameID()).gameID();

        JoinGameRequest joinGameRequestUpdated = new JoinGameRequest(joinGameRequest.playerColor(), displayedID);

        HttpRequest request = buildRequest("PUT", "/game", joinGameRequestUpdated);
        JoinGameResult response = serializer.fromJson(sendRequest(request).body().toString(), JoinGameResult.class);

        return response;

    }

    public ChessGame getGame(int displayedGameNumber) throws Exception {
        try {
            return displayedGameList.get(displayedGameNumber).game();
        } catch (Exception e) {
            handleErrors("Invalid game number");
        }
        return null;
    }

    private HttpRequest buildRequest(String method, String path, Object body){
        var request = HttpRequest.newBuilder().uri(URI.create(serverUrl + path)).setHeader("Content-Type", "application/json");
        if (authToken != null){
            request.header("authorization", authToken);
        }
        if (body != null) {
            String jsonBody = serializer.toJson(body);
            request.method(method, HttpRequest.BodyPublishers.ofString(jsonBody));
        }
        else {
            request.method(method, HttpRequest.BodyPublishers.noBody());
        }
        return request.build();
    }

    public void clear(){
        HttpRequest request = buildRequest("DELETE", "/db", "");
        try {
            sendRequest(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse sendRequest(HttpRequest request) throws Exception {
        HttpResponse response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (Exception e){
            handleErrors(e.getMessage());
        }
        if (response == null || !validResponse(response.statusCode())){
            handleErrors(response.body().toString());
        }

        return response;
    }

    private void handleErrors(String error) throws Exception{
        if (error.contains("author")){
            throw new Exception("Invalid authorization");
        } else if (error.contains("Invalid game number")) {
            throw new Exception("Invalid game number");
        } else {
            throw new Exception("Server Error, please try again");
        }
    }

    private boolean validResponse(int serverStatus){
        return serverStatus / 100 == 2;
    }

    public void connect(GameUI gameUI){

        this.gameUI = gameUI;

        try {
            websocketClient = new WebsocketClient(this);
            websocketClient.send(serializer.toJson(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken,  1)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void leave(){
        try {
            websocketClient.send(serializer.toJson(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, 1)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendLoadGame(){
        gameUI.updateBoard();
    }

    public void sendPrintNotification(String message){
        gameUI.displayNotification(message);
    }


}
