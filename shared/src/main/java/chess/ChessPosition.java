package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    int row;
    int col;
    ChessPiece currentChessPiece;


    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
        currentChessPiece = null;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row - 1;

    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col - 1;
    }

    public void setCurrentChessPiece(ChessPiece chessPiece){
        if(chessPiece != null)
            currentChessPiece = chessPiece;
        else currentChessPiece = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col && Objects.equals(currentChessPiece, that.currentChessPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, currentChessPiece);
    }

}
