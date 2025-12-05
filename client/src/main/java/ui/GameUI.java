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

    public GameUI(ServerFacade serverFacade){
        this.serverFacade = serverFacade;
        scanner = new Scanner(System.in);
    }

    public void run(){
        running = true;
        System.out.println("Enter help for help menu");

        while (running){
            System.out.println("Game\n Enter a command please:\n");
            String command = scanner.nextLine();

            switch (command){
                case "help":
                    break;
                case "redraw chess board", "redraw":
                    break;
                case "make move", "move":
                    break;
                case "resign":
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

    }

    public void updateBoard(ChessGame chessGame, ChessGame.TeamColor teamColor){
        boardPrinter = new BoardPrinter();
        boardPrinter.printBoard(chessGame, teamColor);
    }

}
