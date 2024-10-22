package dataaccess;

import model.UserData;

public interface UserDAO {
    void insertUser (UserData userData);
    UserData getUser (String username);
}
