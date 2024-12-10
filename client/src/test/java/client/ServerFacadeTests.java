package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(3060);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() {
        // Implement a method to clear the database before each test
        server.clear();
    }

    @Test
    public void testRegisterPositive() throws Exception {
        boolean result = facade.register("newUser", "password123", "user@example.com");
        Assertions.assertTrue(result, "Registration should succeed with valid credentials");
    }

    @Test
    public void testRegisterNegative() throws Exception {
        facade.register("existingUser", "password123", "existing@example.com");
        Assertions.assertThrows(Exception.class, () -> {
            facade.register("existingUser", "newPassword", "new@example.com");
        }, "Registration should fail with duplicate username");
    }

    @Test
    public void testLoginPositive() throws Exception {
        facade.register("loginUser", "password123", "login@example.com");
        boolean result = facade.login("loginUser", "password123");
        Assertions.assertTrue(result, "Login should succeed with correct credentials");
    }

    @Test
    public void testLoginNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            facade.login("nonexistentUser", "wrongPassword");
        }, "Login should fail with incorrect credentials");
    }

    @Test
    public void testLogoutPositive() throws Exception {
        facade.register("logoutUser", "password123", "logout@example.com");
        facade.login("logoutUser", "password123");
        boolean result = facade.logout();
        Assertions.assertTrue(result, "Logout should succeed for logged-in user");
    }

    @Test
    public void testLogoutNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            facade.logout();
        }, "Logout should fail when not logged in");
    }

    @Test
    public void testCreateGamePositive() throws Exception {
        facade.register("gameCreator", "password123", "creator@example.com");
        facade.login("gameCreator", "password123");
        int gameId = facade.createGame("Test Game");
        Assertions.assertTrue(gameId > 0, "Game creation should return a valid game ID");
    }

    @Test
    public void testCreateGameNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            facade.createGame("Unauthorized Game");
        }, "Game creation should fail when not logged in");
    }

    @Test
    public void testListGamesPositive() throws Exception {
        facade.register("lister", "password123", "lister@example.com");
        facade.login("lister", "password123");
        facade.createGame("Game 1");
        facade.createGame("Game 2");
        String gameList = facade.listGames();
        Assertions.assertTrue(gameList.contains("Game 1") && gameList.contains("Game 2"),
                "List games should return created games");
    }

    @Test
    public void testJoinGamePositive() throws Exception {
        facade.register("joiner", "password123", "joiner@example.com");
        facade.login("joiner", "password123");
        int gameId = facade.createGame("Join Test");
        boolean result = facade.joinGame(gameId, "WHITE");
        Assertions.assertTrue(result, "Joining a game should succeed with valid game ID and color");
    }

    @Test
    public void testJoinGameNegative() throws Exception {
        facade.register("joiner", "password123", "joiner@example.com");
        facade.login("joiner", "password123");
        Assertions.assertThrows(DataAccessException.class, () -> {
            facade.joinGame(9999, "WHITE");
        }, "Joining a non-existent game should fail");
    }
}