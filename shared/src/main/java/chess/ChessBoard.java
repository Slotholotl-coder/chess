package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    int xValue;
    int yValue;

    ChessPiece[][] coordinates;
    //int[] yCoordinate;

    ChessPiece[] boardGrid;

    public ChessBoard() {
        this.xValue = 1;
        this.yValue = 1;
        //this.xCoordinate = new int[8];
        //this.yCoordinate = new int[8];
        this.coordinates = new ChessPiece[8][8];
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        coordinates[position.getRow()][position.getCol()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return coordinates[position.getRow()][position.getCol()];
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
            coordinates[2][i] = new ChessPiece(ChessGame.TeamColor.White, ChessPiece.PieceType.Pawn);
            coordinates[7][i] = new ChessPiece(ChessGame.TeamColor.Black, ChessPiece.PieceType.Pawn);
    }
}
