package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Spark;

import java.io.Reader;
import java.util.Collection;
import java.util.List;


public class Server {
    private UserService userService = new UserService();
    private GameService gameService = new GameService();
    private Gson gson = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //Register
        Spark.post("/user", (request, response) -> {
            try {
                UserData user = gson.fromJson(request.body(), UserData.class);
                AuthData authData = userService.register(user);
                response.status(200);
                return "{ \"username\": \"" + user.getUsername() + "\", \"authToken\": \"" + authData.getAuthToken() + "\" }";
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Already Taken")) {
                    response.status(403);
                    return "{\"message\": \"Error: already taken\"}";
                } else {
                    response.status(400);
                    return "{\"message\": \"Error: bad request\"}";
                }
            } catch (Exception e) {
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        });

        //Login
        Spark.post("/session", (req, res) -> {
            try {
                UserData user = gson.fromJson(req.body(), UserData.class);
                AuthData authData = userService.login(user);
                return gson.toJson(authData);
            } catch (RuntimeException e) {
                res.status(401);
                return "{\"message\": \"Error: Unauthorized" + "\"}";
            } catch (Exception e) {
                res.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        });

        //Logout
        Spark.delete("/session", (request, response) -> {
            try {
                String username = request.headers("authorization");
                userService.logout(username);
                response.status(200);
                return "{}";
            } catch (RuntimeException e) {
                response.status(401);
                return "{\"message\": \"Error: unauthorized" + "\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        });

        //Clear
        Spark.delete("/db", ((request, response) -> {
            try {
                userService.clear();
                response.status(200);
                return "{}";
            } catch (Exception e) {
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        }));

        //List games
        Spark.get("/game", (request, response) -> {
            try {
                String authToken = request.headers("authorization");
                if (authToken == null || authToken.isEmpty()) {
                    response.status(401);
                    return "{\"message\": \"Error: unauthorized\"}";
                }
                Collection<GameData> games = gameService.listGames(authToken);
                response.status(200);
                return gson.toJson(games);
            } catch (RuntimeException e) {
                response.status(401);
                return "{\"message\": \"Error: unauthorized" + "\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        });

        //Create game
        Spark.post("/game", (request, response) -> {
            try {
                String authToken = request.headers("authorization");
                String requestBody = request.body();

                // Extract the gameName from the request body
                GameData gameData = gson.fromJson(requestBody, GameData.class);
                String gameName = gameData.getGameName(); // Use the getter to get the gameName

                // Validate inputs
                if (authToken == null || authToken.isEmpty() || gameName == null || gameName.isEmpty()) {
                    response.status(400);
                    return "{\"message\": \"Error: bad request\"}";
                }

                // Create the game
                gameService.createGame(authToken, gameName);
                response.status(200);
                return "{}"; // Return an empty response for success
            } catch (RuntimeException e) {
                if (e.getMessage().contains("unauthorized")) {
                    response.status(401);
                    return "{\"message\": \"Error: unauthorized\"}";
                } else {
                    response.status(403);
                    return "{\"message\": \"Error: already taken\"}";
                }
            } catch (Exception e) {
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop(){
        Spark.stop();
        Spark.awaitStop();
    }

}