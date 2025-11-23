package ui;

import chess.ChessGame;
import serverfacade.ServerFacade;

public class GameUI {

    ServerFacade serverFacade;

    BoardPrinter boardPrinter;

    ChessGame.TeamColor teamColor;

    ChessGame chessGame;

    public GameUI(){
    }

    public void updateBoard(ChessGame chessGame, ChessGame.TeamColor teamColor){
        boardPrinter = new BoardPrinter();
        boardPrinter.printBoard(chessGame, teamColor);
    }

}
