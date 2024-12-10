package ui;

import server.ServerFacade;

import java.util.Scanner;

public class PostloginUI {
    private final ServerFacade server;
    private final Scanner scanner;

    public PostloginUI(ServerFacade server) {
        this.server = server;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n[POSTLOGIN] Please enter a command (help, logout, create, list, join, observe):");
            String command = scanner.nextLine().toLowerCase().trim();

            try {
                switch (command) {
                    case "help":
                        displayHelp();
                        break;
                    case "logout":
                        if (logout()) {
                            running = false;
                        }
                        break;
                    case "create":
                        createGame();
                        break;
                    case "list":
                        listGames();
                        break;
                    case "join":
                        joinGame();
                        break;
                    case "observe":
                        observeGame();
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
        System.out.println("  logout   - Log out and return to the main menu");
        System.out.println("  create   - Create a new game");
        System.out.println("  list     - List all available games");
        System.out.println("  join     - Join an existing game");
        System.out.println("  observe  - Observe an existing game");
    }

    private boolean logout() throws Exception {
        if (server.logout()) {
            System.out.println("Logged out successfully.");
            return true;
        }
        System.out.println("Logout failed.");
        return false;
    }

    private void createGame() throws Exception {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();
        int gameId = server.createGame(gameName);
        System.out.println("Game created successfully. Game ID: " + gameId);
    }

    private void listGames() throws Exception {
        String gameList = server.listGames();
        System.out.println("Available games:");
        System.out.println(gameList);
    }

    private void joinGame() throws Exception {
        System.out.print("Enter game number: ");
        int gameNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter color (WHITE/BLACK): ");
        String color = scanner.nextLine().toUpperCase();

        if (server.joinGame(gameNumber, color)) {
            System.out.println("Joined game successfully.");
            drawChessboard(color);
        } else {
            System.out.println("Failed to join game.");
        }
    }

    private void observeGame() throws Exception {
        System.out.print("Enter game number to observe: ");
        int gameNumber = Integer.parseInt(scanner.nextLine());

        if (server.joinGame(gameNumber, null)) {
            System.out.println("Observing game successfully.");
            drawChessboard("WHITE");
        } else {
            System.out.println("Failed to observe game.");
        }
    }

    private void drawChessboard(String perspective) {
        // Implement chessboard drawing logic here
        System.out.println("Drawing chessboard from " + perspective + " perspective");
        // Add your chessboard drawing code
    }
}