package service;

import dataaccess.*;

public class ServiceTest {

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    UserService userService;

    GameService gameService;

    public ServiceTest(){
        userService = new UserService(userDAO, authDAO);

        gameService = new GameService(userDAO, authDAO, gameDAO);

    }

}
