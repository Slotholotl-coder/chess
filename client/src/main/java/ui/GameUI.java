package ui;

import chess.ChessGame;
import serverFacade.ServerFacade;

public class GameUI {

    ServerFacade serverFacade;

    BoardPrinter boardPrinter;

    ChessGame.TeamColor teamColor;

    ChessGame chessGame;

    public GameUI(){
//        this.serverFacade = serverFacade;
//        boardPrinter = new BoardPrinter();
//        this.teamColor = teamColor;
//        this.chessGame = chessGame;

        //updateBoard(chessGame, teamColor);

    }

    public void updateBoard(ChessGame chessGame, ChessGame.TeamColor teamColor){
        boardPrinter.printBoard(chessGame, teamColor);
    }

}
