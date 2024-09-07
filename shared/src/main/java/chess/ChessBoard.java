package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {


    ChessPiece[][] coordinates;
    //int[] yCoordinate;


    public ChessBoard() {
        //this.xCoordinate = new int[8];
        //this.yCoordinate = new int[8];
        this.coordinates = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        coordinates[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return coordinates[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Clear board
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                coordinates[x][y] = null;
            }
        }
        //Set Pawns
        for (int i = 0; i < 8; i++) {
            coordinates[i][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            coordinates[i][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        coordinates[0][0] = coordinates[7][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        coordinates[0][7] = coordinates[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        coordinates[1][0] = coordinates[6][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        coordinates[1][7] = coordinates[6][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        coordinates[2][0] = coordinates[5][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        coordinates[2][7] = coordinates[5][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        coordinates[3][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        coordinates[3][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        coordinates[4][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        coordinates[4][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(coordinates[y][x] != null) {
                    System.out.print(coordinates[y][x].getPieceType() + ((coordinates[y][x].getTeamColor() == ChessGame.TeamColor.WHITE) ? "W " : "B "));
                }
                else System.out.print("  |  ");
            }
            System.out.println();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(coordinates);
    }
}
