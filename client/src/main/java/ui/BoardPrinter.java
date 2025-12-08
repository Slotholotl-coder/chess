package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import ui.EscapeSequences;

import java.util.HashMap;

public class BoardPrinter {


    static HashMap<ChessPiece.PieceType, String> blackPieces;

    static HashMap<ChessPiece.PieceType, String> whitePieces;


    public BoardPrinter(){
        blackPieces = new HashMap<ChessPiece.PieceType, String>() {{
            put(ChessPiece.PieceType.KING, EscapeSequences.BLACK_KING);
            put(ChessPiece.PieceType.QUEEN, EscapeSequences.BLACK_QUEEN);
            put(ChessPiece.PieceType.BISHOP, EscapeSequences.BLACK_BISHOP);
            put(ChessPiece.PieceType.ROOK, EscapeSequences.BLACK_ROOK);
            put(ChessPiece.PieceType.KNIGHT, EscapeSequences.BLACK_KNIGHT);
            put(ChessPiece.PieceType.PAWN, EscapeSequences.BLACK_PAWN);
        }};

        whitePieces = new HashMap<ChessPiece.PieceType, String>() {{
            put(ChessPiece.PieceType.KING, EscapeSequences.WHITE_KING);
            put(ChessPiece.PieceType.QUEEN, EscapeSequences.WHITE_QUEEN);
            put(ChessPiece.PieceType.BISHOP, EscapeSequences.WHITE_BISHOP);
            put(ChessPiece.PieceType.ROOK, EscapeSequences.WHITE_ROOK);
            put(ChessPiece.PieceType.KNIGHT, EscapeSequences.WHITE_KNIGHT);
            put(ChessPiece.PieceType.PAWN, EscapeSequences.WHITE_PAWN);
        }};
    }



    public void printBoard(ChessGame game, ChessGame.TeamColor teamColor){

        //System.out.print(EscapeSequences.SET_TEXT_BOLD);

        int direction = teamColor == ChessGame.TeamColor.BLACK ? 1 : -8;

        int top = teamColor == ChessGame.TeamColor.BLACK ? 8 : -1;

        ChessBoard chessBoard = game.getBoard();

        printLetterPositioning(teamColor == ChessGame.TeamColor.BLACK);
        for (int y = direction; y <= top; y++) {
            for (int x = 1; x <= 8; x++) {

                if (x == 1) {
                    System.out.print(" " + Math.abs(y) + " ");
                }

                int columnEven = x % 2;
                int rowEven = y % 2;
                int flip = direction % 2;

                setSquareColor(columnEven, rowEven, flip);

                int vertical = Math.abs(y);
                if (chessBoard.getPiece(new ChessPosition(vertical, x)) != null) {
                    ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(vertical, x));
                    System.out.print(chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                            EscapeSequences.SET_TEXT_COLOR_BLUE : EscapeSequences.SET_TEXT_COLOR_RED);
                    System.out.print(chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                                    whitePieces.get(chessPiece.getPieceType()) : blackPieces.get(chessPiece.getPieceType()));
                } else{
                    System.out.print(EscapeSequences.EMPTY);
                }
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                System.out.print(EscapeSequences.RESET_BG_COLOR);
                if (x == 8) {
                    System.out.print(" " + Math.abs(y) + " ");
                }
            }
            System.out.print(EscapeSequences.EMPTY);
            System.out.println();
        }
        printLetterPositioning(teamColor == ChessGame.TeamColor.BLACK);
    }

    private void printLetterPositioning(boolean reverse){
        String letters;
        System.out.print("   ");
        if (reverse){
            letters = "hgfedcba";
        } else {
            letters = "abcdefgh";
        }
        for (int x = 1; x <= 8; x++) {
            System.out.print(" " + letters.charAt(x - 1) + "  ");
        }
        System.out.println();
    }

    private static void setSquareColor(int columnEven, int rowEven, int flip) {
        if (columnEven == flip && rowEven == flip || columnEven != flip && rowEven != flip){
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        }
        else{
            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        }
    }

}