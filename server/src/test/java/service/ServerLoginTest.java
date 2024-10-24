package service;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerLoginTest {
    private static UserService userService;

    @BeforeAll
    static void setUp() {
        userService = new UserService();
        userService.register(new UserData("testUser", "testPass", "test@example.com"));
    }

    @Test
    void testLoginSuccess() {
        AuthData authData = userService.login(new UserData("testUser", "testPass", null));
        assertNotNull(authData.getAuthToken());
        assertEquals("testUser", authData.getUsername());
    }

    @Test
    void testLoginFailure() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> userService.login(new UserData("testUser", "wrongPass", null)));

        assertEquals("Unauthorized", exception.getMessage());
    }
}
