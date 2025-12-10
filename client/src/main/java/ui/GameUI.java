package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import serverfacade.ServerFacade;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class GameUI {

    ServerFacade serverFacade;

    BoardPrinter boardPrinter;

    ChessGame.TeamColor teamColor;

    ChessGame chessGame;

    boolean running;

    Scanner scanner;

    boolean isObserver;
    boolean resigned;

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

            if (!isObserver && !resigned) {
                switch (command) {
                    case "make move", "move":
                        makeMove();
                        break;
                    case "resign":
                        resign();
                        break;
                }
            }
            switch (command){
                case "help":
                    displayHelpMenu();
                    break;
                case "redraw chess board", "redraw":
                    updateBoard();
                    break;
                case "highlight legal moves", "highlight":
                    highlight();
                    break;
                case "quit", "leave":
                    leave();
                    running = false;
                    break;
            }
        }
    }

    private void highlight(){
        System.out.println("Enter a position to highlight moves for: ex: a5");
        String move = scanner.nextLine();
        if (move.length() != 2){
            System.out.println("Invalid position format");
            return;
        }

        char startColumnChar = move.charAt(0);
        int startColumn = startColumnChar - 'a' + 1;

        int startRow = Integer.parseInt(String.valueOf(move.charAt(1)));

        ChessPosition chessPosition = new ChessPosition(startRow, startColumn);

        Collection<ChessMove> possibleChessMoves = chessGame.validMoves(chessPosition);

        if (possibleChessMoves == null){
            System.out.println("Invalid starting position");
            return;
        }

        boardPrinter.setHighlightMoves(possibleChessMoves);

        updateBoard();

        boardPrinter.setHighlightMoves(null);

    }

    private void resign(){
        System.out.println("Are you sure? yes or no");
        String response = scanner.nextLine();
        if (Objects.equals(response, "yes")){
            serverFacade.resign();
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
    
    private boolean isPromotionalMove(ChessPosition startPosition, ChessPosition endPosition){
        if (endPosition.getRow() == 8 || endPosition.getRow() == 1) {
            return chessGame.getBoard().getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN;
        }
        return false;
    }
    
    private ChessPiece.PieceType getPromotionPiece(String promotionString){
        switch (promotionString.toLowerCase()){
            case "queen" -> {
                return ChessPiece.PieceType.QUEEN;
            }
            case "bishop" -> {
                return ChessPiece.PieceType.BISHOP;
            }
            case "rook" -> {
                return ChessPiece.PieceType.ROOK;
            }
            case "knight" -> {
                return ChessPiece.PieceType.KNIGHT;
            }
            default -> {
                return null;
            }
        }
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

        ChessPiece.PieceType promotionPiece = null;
        if (isPromotionalMove(startPosition, endPosition)){
            System.out.println("Choose a piece to promote to: (queen, bishop, rook, knight)");
            String promotionString = scanner.nextLine();
            promotionPiece = getPromotionPiece(promotionString);
            if (promotionPiece == null){
                System.out.println("Invalid promotion piece");
                return;
            }
        }

        ChessMove chessMove = new ChessMove(startPosition, endPosition, promotionPiece);

        Collection<ChessMove> possibleChessMoves = chessGame.validMoves(startPosition);

        if (possibleChessMoves == null){
            System.out.println("Invalid starting position");
            return;
        }

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

    public void updateResign(boolean gameOver){
        resigned = gameOver;
    }

    private void leave(){
        serverFacade.leave();
    }

    private void updateBoard(){
        boardPrinter.printBoard(chessGame, teamColor);
    }

    public void displayNotification(String message){
        if (message != null) {
            System.out.println(message);
        }
    }

}
