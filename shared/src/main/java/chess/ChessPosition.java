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
<<<<<<< HEAD
    ChessPiece currentChessPiece;
=======
>>>>>>> 77252ce (6)


    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
<<<<<<< HEAD
        currentChessPiece = null;
=======
>>>>>>> 77252ce (6)
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

<<<<<<< HEAD
    public void setCurrentChessPiece(ChessPiece chessPiece){
        if(chessPiece != null)
            currentChessPiece = chessPiece;
        else currentChessPiece = null;
    }

=======
>>>>>>> 77252ce (6)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
<<<<<<< HEAD
        return row == that.row && col == that.col && Objects.equals(currentChessPiece, that.currentChessPiece);
=======
        return row == that.row && col == that.col;
>>>>>>> 77252ce (6)
    }

    @Override
    public int hashCode() {
<<<<<<< HEAD
        return Objects.hash(row, col, currentChessPiece);
    }

=======
        return Objects.hash(row, col);
    }
>>>>>>> 77252ce (6)
}
