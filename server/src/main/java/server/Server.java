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

            Spark.post("/user", (req, res) -> {
                UserData user = gson.fromJson(req.body(), UserData.class);
                AuthData authData = userService.login(user);
                return gson.toJson(authData);
            });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop(){
        Spark.stop();
    }
}