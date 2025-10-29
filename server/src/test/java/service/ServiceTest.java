package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;

public class ServiceTest {

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    UserService userService;

    public ServiceTest(){
        userService = new UserService(userDAO, authDAO);
    }

}
