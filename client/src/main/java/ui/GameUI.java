package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import serverFacade.ServerFacade;

import java.util.Collection;
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
                    case "make move", "move":
                        makeMove();
                        break;
                    case "resign":
                        break;
                }
            }
            switch (command){
                case "help":
                    displayHelpMenu();
                    break;
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

    private void displayHelpMenu(){
        System.out.println("Available Commands:\n" +
                "help - Display this help menu\n" +
                "make move - make a move\n" +
                "resign - resign\n" +
                "redraw - redraw chess board\n" +
                "highlight - highlight legal moves\n" +
                "quit - Exit");
    }

    public void makeMove(){
        if (chessGame.getTeamTurn() != teamColor){
            System.out.println("It's not your turn yo");
            return;
        }

        System.out.println("Enter move - format a5 b6");
        String move = scanner.nextLine();
        if (move.length() != 5){
            System.out.println("Invalid move format");
            return;
        }

        char startColumnChar = move.charAt(0);
        char endColumnChar = move.charAt(3);

        int startColumn = startColumnChar - 'a' + 1;
        int endColumn = endColumnChar - 'a' + 1;

        int startRow = Integer.parseInt(String.valueOf(move.charAt(1)));
        int endRow = Integer.parseInt(String.valueOf(move.charAt(4)));

        ChessPosition startPosition = new ChessPosition(startRow, startColumn);
        ChessPosition endPosition = new ChessPosition(endRow, endColumn);

        ChessMove chessMove = new ChessMove(startPosition, endPosition, null);

        Collection<ChessMove> possibleChessMoves = chessGame.validMoves(startPosition);

        boolean validMove = false;

        for (ChessMove possibleMove : possibleChessMoves){
            if (possibleMove.equals(chessMove)){
                validMove = true;
                break;
            }
        }

        if (!validMove){
            System.out.println("Not a valid move");
            return;
        }

        serverFacade.makeMove(chessMove);

    }

    public void updateGame(ChessGame chessGame){
        if (chessGame != null) {
            this.chessGame = chessGame;
        }
        updateBoard();
    }

    private void leave(){
        serverFacade.leave();
    }

    private void updateBoard(){
        boardPrinter.printBoard(chessGame, teamColor);
    }

    public void displayNotification(String message){
        System.out.println(message);
    }

}
