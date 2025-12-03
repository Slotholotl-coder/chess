package ui;

import chess.ChessGame;
import serverFacade.ServerFacade;

import java.io.IOException;
import java.util.Scanner;

public class GameUI {

    ServerFacade serverFacade;

    BoardPrinter boardPrinter;

    ChessGame.TeamColor teamColor;

    ChessGame chessGame;

    boolean running;

    Scanner scanner = new Scanner(System.in);

    public GameUI(ServerFacade serverFacade, ChessGame chessGame, ChessGame.TeamColor teamColor){
        this.serverFacade = serverFacade;
        boardPrinter = new BoardPrinter();
        this.teamColor = teamColor;
        this.chessGame = chessGame;

        updateBoard();
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
                case "redraw chess board":
                    updateBoard();
                    break;
                case "make move":
                    break;
                case "resign":
                    break;
                case "highlight legal moves":
                    break;
                case "quit", "leave":
                    leave();
                    running = false;
                    break;
            }

        }

    }

    private void leave(){
        try {
            serverFacade.leave(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void updateBoard(){
        boardPrinter.printBoard(chessGame, teamColor);
    }

}
