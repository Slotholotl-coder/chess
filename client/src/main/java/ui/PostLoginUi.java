package ui;

import chess.ChessGame;
import model.*;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class PostLoginUi {

    ServerFacade serverFacade;

    Scanner scanner;

    PreLoginUI preLoginUI;

    boolean running;

    public PostLoginUi(ServerFacade serverFacade, PreLoginUI preLoginUI){
        this.serverFacade = serverFacade;
        scanner = new Scanner(System.in);
        this.preLoginUI = preLoginUI;
    }

    public void run(){
        running = true;
        System.out.println("Enter help for help menu");

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
                case "list games":
                    listGames();
                    break;
                case "create game":
                    createGame();
                    break;
                case "play game":
                    joinGame();
                    break;
                case "observe game":
                    observeGame();
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
                "list games- List Games\n" +
                "create game - Create game\n" +
                "play game - Join game\n" +
                "observe game - watch a game\n" +
                "quit - Exit");
    }

    private void logout(){
        try {
            serverFacade.logout();
            running = false;
        } catch (Exception e) {
            printError(e);
        }
    }

    private void listGames(){
        try {
            ListGamesResult listGamesResult = serverFacade.listGames();
            System.out.println(listGamesResult.toString());
        } catch (Exception e) {
            printError(e);
        }
    }

    private void createGame(){
        System.out.println("Enter Game Name:");
        String gameName = scanner.nextLine();

        try {
            CreateGameResult createGameResult = serverFacade.createGame(new CreateGameRequest(null, gameName));
            System.out.println("Game Created");
        } catch (Exception e) {
            printError(e);
        }
    }

    private void joinGame(){
        System.out.println("Game Number:");
        int gameID = Integer.parseInt(scanner.nextLine());
        System.out.println("Join Which Side? black/white");
        String teamColor = scanner.nextLine().toUpperCase();

        ChessGame.TeamColor joinedColor = teamColor.equals("BlACK") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

        try {
            JoinGameResult joinGameResult = serverFacade.joinGame(new JoinGameRequest(teamColor, gameID));

            GameUI gameUI = new GameUI(serverFacade, joinGameResult.game().game(), joinedColor, false);

            serverFacade.connect(gameUI);

            gameUI.run();
        } catch (Exception e) {
            printError(e);
        }

    }

    private void observeGame(){
        System.out.println("Enter Game Number:");
        int gameNumber = Integer.parseInt(scanner.nextLine());

        try {
            ChessGame chessGame = serverFacade.getGame(gameNumber);

            GameUI gameUI = new GameUI(serverFacade, chessGame, ChessGame.TeamColor.WHITE, true);

            serverFacade.resetTeamColor();
            serverFacade.connect(gameUI);

            gameUI.run();

        } catch (Exception e) {
            printError(e);
        }

    }

    private void printError(Exception e){
        System.out.println(e.getMessage());
    }


}
