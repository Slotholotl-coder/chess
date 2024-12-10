package ui;

import model.AuthData;
import server.ServerFacade;

import java.util.Scanner;

public class PreloginUI {
    private final ServerFacade server;
    private final Scanner scanner;

    public PreloginUI(String serverUrl) {
        server = new ServerFacade(serverUrl);
        scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n[PRELOGIN] Please enter a command (help, quit, login, register):");
            String command = scanner.nextLine().toLowerCase().trim();

            try {
                switch (command) {
                    case "help":
                        displayHelp();
                        break;
                    case "quit":
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    case "login":
                        login();
                        break;
                    case "register":
                        register();
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for a list of commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  help     - Display this help message");
        System.out.println("  quit     - Exit the program");
        System.out.println("  login    - Log in to an existing account");
        System.out.println("  register - Create a new account");
    }

    private void login() throws Exception {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if(!server.login(username, password)){
            System.out.println("Invalid username or password");
            return;
        }
        System.out.println("Login successful. Welcome, " + username + "!");
        // Transition to PostloginUI
        PostloginUI postloginUI = new PostloginUI(server);
        postloginUI.run();
    }

    private void register() throws Exception {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        if(!server.register(username, password, email)){
            System.out.println("Issue registering user. Username may be taken");
            return;
        }
        System.out.println("Registration successful. Welcome, " + username + "!");
        // Transition to PostloginUI
        PostloginUI postloginUI = new PostloginUI(server);
        postloginUI.run();
    }

    public static void main(String[] args) {
        String serverUrl = "http://localhost:3060"; // Replace with your server URL
        PreloginUI ui = new PreloginUI(serverUrl);
        ui.run();
    }
}