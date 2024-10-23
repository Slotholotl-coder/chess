package dataaccess;

import model.UserData;

public interface UserDAO {
    void insertUser (UserData userData);
    UserData getUser (String username);
    void removeUser(String username);
    void clear();
}
