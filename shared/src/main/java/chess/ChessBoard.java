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

    //9/7

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
        coordinates[height(position.getRow())][height(position.getColumn())] = piece;
    }

    public ChessPiece[][] getCoordinates(){
        return coordinates;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (position.getRow() > 8 || position.getRow() < 1 || position.getColumn() > 8 || position.getColumn() < 1){
            throw new IndexOutOfBoundsException("getPiece() needs a position within the game board");
        }else
            return coordinates[height(position.getRow())][width(position.getColumn())];
    }

    public int height(int index){
        return index - 1;
    }
    public int width(int index){
        return index-1;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Clear board
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                coordinates[height(x)][width(y)] = null;
            }
        }
        //Set Pawns
        for (int i = 1; i <= 8; i++) {
            coordinates[height(2)][width(i)] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            coordinates[height(7)][width(i)] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
          coordinates[height(1)][width(1)] = coordinates[height(1)][width(8)] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
          coordinates[height(8)][width(1)] = coordinates[height(8)][width(8)] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
          coordinates[height(1)][width(2)] = coordinates[height(1)][width(7)] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
          coordinates[height(8)][width(2)] = coordinates[height(8)][width(7)] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
          coordinates[height(1)][width(3)] = coordinates[height(1)][width(6)] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
          coordinates[height(8)][width(3)] = coordinates[height(8)][width(6)] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);

          coordinates[height(1)][width(4)] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
          coordinates[height(8)][width(4)] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
          coordinates[height(1)][width(5)] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
          coordinates[height(8)][width(5)] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

//        for(int x = 7; x >= 0; x--) {
//            for(int y = 0; y < 8; y++) {
//                if(coordinates[x][y] != null) {
//                    System.out.print(coordinates[x][y].getPieceType() + ((coordinates[x][y].getTeamColor() == ChessGame.TeamColor.WHITE) ? "W " : "B ") + x +y);
//                }
//                else System.out.print("  |  " +x+y);
//            }
//            System.out.println();
//        }
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
