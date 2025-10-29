package service;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import server.ClearAllDataHandler;

public class ClearTests extends ServiceTest{

    @Test
    void clearTest() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        assert (gameDAO.getAllGames().isEmpty());
    }

}
