package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Spark;


public class Server {
    private UserService userService = new UserService();
    private Gson gson = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //Register
        Spark.post("/user", (request, response) -> {
            UserData user = gson.fromJson(request.body(), UserData.class);
            userService.register(user);
            response.status(201);
            return  "User registered";
        });

        //Login
        Spark.post("/user", (req, res) -> {
            UserData user = gson.fromJson(req.body(), UserData.class);
            AuthData authData = userService.login(user);
            return gson.toJson(authData);
        });

        //Delete
        Spark.delete("/db", ((request, response) -> {
            String authToken = request.headers("authorization");
            if (authToken == null || authToken.isEmpty()){
                response.status(401);
                return "{\"message\": \"Unathorized\")";
            }
            userService.logout(authToken);
            response.status(200);
            return "{}";
        }));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop(){
        Spark.stop();
        Spark.awaitStop();
    }
}