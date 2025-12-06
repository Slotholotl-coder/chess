package ui;

import chess.ChessGame;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class GameUI {

    ServerFacade serverFacade;

    BoardPrinter boardPrinter;

    ChessGame.TeamColor teamColor;

    ChessGame chessGame;

    boolean running;

    Scanner scanner;

    boolean isObserver;

    public GameUI(ServerFacade serverFacade, ChessGame chessGame, ChessGame.TeamColor teamColor, boolean isObserver){
        this.serverFacade = serverFacade;
        scanner = new Scanner(System.in);
        boardPrinter = new BoardPrinter();
        this.chessGame = chessGame;
        this.teamColor = teamColor;
        this.isObserver = isObserver;
    }

    public void run(){
        running = true;
        System.out.println("Enter help for help menu");

        while (running){
            System.out.println("Game\n Enter a command please:\n");
            String command = scanner.nextLine();

            if (!isObserver) {
                switch (command) {
                    case "help":
                        break;
                    case "make move", "move":
                        break;
                    case "resign":
                        break;
                }
            }
            switch (command){
                case "redraw chess board", "redraw":
                    break;
                case "highlight legal moves", "highlight":
                    break;
                case "quit", "leave":
                    leave();
                    running = false;
                    break;
            }
        }
    }

    private void leave(){
        serverFacade.leave();
    }

    public void updateBoard(){
        boardPrinter.printBoard(chessGame, teamColor);
    }

    public void displayNotification(String message){
        System.out.println(message);
    }

}
