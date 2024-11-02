package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class mySQLUserDAO {
    public void insertUser(User user) {
        String INSERT_USERS_SQL = "INSERT INTO users (id, pwd, fullname, email) VALUES (?, ?, ?, ?);";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {

            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullname());
            preparedStatement.setString(4, user.getEmail());

            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("User inserted successfully!");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
}
