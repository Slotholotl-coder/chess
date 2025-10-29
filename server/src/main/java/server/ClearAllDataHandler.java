package server;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import service.UserService;

public class ClearAllDataHandler {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;


    public ClearAllDataHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clear(Context context) {
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            context.status(500);
            context.json("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

}
