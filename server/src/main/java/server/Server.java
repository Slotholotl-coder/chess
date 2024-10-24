package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Spark;

import java.util.Collection;


public class Server {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final Gson gson = new Gson();

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
                return "{ \"username\": \"" + authData.getUsername() + "\", \"authToken\": \"" + authData.getAuthToken() + "\" }";
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
                String authToken = request.headers("authorization");
                System.out.println(authToken);
                userService.logout(authToken);
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


        //Create game
        Spark.post("/game", (request, response) -> {
            try {
                String authToken = request.headers("authorization");
                String requestBody = request.body();

                // Extract the gameName from the request body
                GameData gameData = gson.fromJson(requestBody, GameData.class);
                String gameName = gameData.getGameName();

                // Validate inputs
                if (authToken == null || authToken.isEmpty() || gameName == null || gameName.isEmpty()) {
                    response.status(400);
                    return "{\"message\": \"Error: bad request\"}";
                }

                // Create the game
                int gameId = gameService.createGame(authToken, gameName);
                response.status(200);
                System.out.println(gameId);
                return "{ \"gameID\": \"" + gameId + "\"}";
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
                System.out.println(gson.toJson(new GameResponse(games)));
                return gson.toJson(new GameResponse(games));
            } catch (RuntimeException e) {
                response.status(401);
                return "{\"message\": \"Error: unauthorized" + "\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        });

        //Join game
        Spark.put("/game", (request, response) -> {
            String authToken = request.headers("authorization");
            if (authToken == null) {
                response.status(401);
                return "{\"message\": \"Error: unauthorized\"}";
            }
            try {
                JoinGameRequest joinRequest = gson.fromJson(request.body(), JoinGameRequest.class);

                gameService.joinGame(authToken, joinRequest.getGameID(), joinRequest.getPlayerColor());
                response.status(200);
                return "{}";
            } catch (RuntimeException e) {
                if (e.getMessage().contains("unauthorized")) {
                    response.status(401);
                    return "{\"message\": \"Error: unauthorized\"}";
                } else if (e.getMessage().contains("already taken")) {
                    response.status(403);
                    return "{\"message\": \"Error: already taken\"}";
                }
                response.status(400);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            } catch (Exception e) {
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        });

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private static class JoinGameRequest {
        private String playerColor;
        private int gameID;

        public String getPlayerColor() {
            return playerColor;
        }

        public int getGameID() {
            return gameID;
        }
    }

    public static class GameResponse {
        Collection<GameData> games;

        public GameResponse(Collection<GameData> games) {
            this.games = games;
        }

        public Collection<GameData> getGames() {
            return games;
        }

    }

}