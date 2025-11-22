package client;

import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;
import ui.PostLoginUi;
import ui.PreLoginUI;

import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static PreLoginUI preLoginUI;
    private static PostLoginUi postLoginUi;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        preLoginUI = new PreLoginUI(facade);
        postLoginUi = new PostLoginUi(facade, preLoginUI);
    }

    @BeforeEach
    public void clear(){
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerPositive() throws Exception {
        assertDoesNotThrow( () -> facade.register(new RegisterRequest("player1", "password", "p1@email.com")));
    }

    @Test
    void registerNegative() throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertThrows(Exception.class, () -> facade.register(new RegisterRequest("player1", "password", "p1@email.com")));
    }

    @Test
    void loginPositive() throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        facade.logout();
        assertDoesNotThrow(() -> facade.login(new LoginRequest("player1", "password")));
    }

    @Test
    void loginNegative() throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertThrows(Exception.class, () -> facade.login(new LoginRequest("player1", "afds")));
    }

    @Test
    void logoutPositive() throws Exception {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertDoesNotThrow( () -> facade.logout());
    }

    @Test
    void logoutNegative() throws Exception {
        assertThrows(Exception.class, () -> facade.logout());
    }

}
