package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.UserService;

import static spark.Spark.*;

public class Server {
    private UserService userService = new UserService();
    private Gson gson = new Gson();

    public void main(String[] args){
        port(8080);

        post("/user", (req, res) -> {
            UserData user = gson.fromJson(req.body(), UserData.class);
            AuthData authData = userService.login(user);
            return gson.toJson(authData);
        });
    }

}