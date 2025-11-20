package ui;

import model.*;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class PostLoginUi {

    ServerFacade serverFacade;

    Scanner scanner;

    public PostLoginUi(ServerFacade serverFacade){
        this.serverFacade = serverFacade;
        scanner = new Scanner(System.in);
    }

    public void run(){
        boolean running = true;

        while (running){
            System.out.println("Postlogin\n Enter a command please:\n");
            String command = scanner.nextLine();

            switch (command){
                case "help":
                    displayHelpMenu();
                    break;
                case "logout":
                    logout();
                    break;
                case "list":
                    listGames();
                    break;
                case "create":
                    createGame();
                    break;
                case "join":
                    joinGame();
                    break;
                case "quit":
                    running = false;
                    break;
            }

        }

    }

    private void displayHelpMenu(){
        System.out.println("Available Commands:\n" +
                "help - Display this help menu\n" +
                "logout - Logout and return to login menu\n" +
                "list - List Games\n" +
                "create - Create game\n" +
                "join - Join game" +
                "quit - Exit");
    }

    private void logout(){
        serverFacade.logout(new LogoutRequest(null));
    }

    private void listGames(){
       ListGamesResult listGamesResult = serverFacade.listGames(null);
       System.out.println(listGamesResult);
    }

    private void createGame(){
        System.out.println("Enter Game Name:");
        String gameName = scanner.nextLine();

        serverFacade.createGame(new CreateGameRequest(null, gameName));
    }

    private void joinGame(){
        System.out.println("Game Number:");
        int gameID = Integer.parseInt(scanner.nextLine());
        System.out.println("Join Which Side? black/white");
        String teamColor = scanner.nextLine();

        serverFacade.joinGame(new JoinGameRequest(teamColor, gameID));
    }

}
