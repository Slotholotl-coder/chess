package java.service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import service.UserService;

public abstract class ServiceTest {

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    UserService userService;

    public ServiceTest(){
        userService = new UserService(userDAO, authDAO);
    }

}
