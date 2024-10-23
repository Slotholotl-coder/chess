package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Spark;

import java.io.Reader;


public class Server {
    private UserService userService = new UserService();
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
                if (e.getMessage().contains("Already Taken")){
                    response.status(403);
                    return "{\"message\": \"Error: already taken\"}";
                }
                else {
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
                String username = String.valueOf(gson.fromJson((Reader) request.headers(), UserData.class));
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
            }
            catch (Exception e){
                response.status(500);
                return "{\"message\": \"Error: " + e.getMessage() + "\"}";
            }
        }));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop(){
        Spark.stop();
        Spark.awaitStop();
    }
}