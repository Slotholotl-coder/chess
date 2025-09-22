package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;

    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch (pieceType){
            case QUEEN:
                bishopMoves(board, myPosition, moves);
                rookMoves(board, myPosition, moves);

                break;
            case KING:
                kingMoves(board, myPosition, moves);

                break;
            case PAWN:
                pawnMoves(board, myPosition, moves);

                break;
            case ROOK:
                rookMoves(board, myPosition, moves);

                break;
            case BISHOP:
                bishopMoves(board, myPosition, moves);

                break;
            case KNIGHT:
                knightMoves(board, myPosition, moves);

                break;
        }

        return moves;
    }

    private void pawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves){
        switch (pieceColor){
            case WHITE:
                if (myPosition.getRow() == 2){
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    if (!isBlocked(board, moveToPosition) && !isBlocked(board, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()))){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                } else if (myPosition.getRow() == 7) {
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (!isBlocked(board, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.KNIGHT));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.KNIGHT));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.KNIGHT));
                    }
                }
                if (myPosition.getRow() != 7){
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (isInBounds(moveToPosition) && !isBlocked(board, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                }
                break;
            case BLACK:
                if (myPosition.getRow() == 7){
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if (!isBlocked(board, moveToPosition) && !isBlocked(board, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()))){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                } else if (myPosition.getRow() == 2) {
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (!isBlocked(board, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.KNIGHT));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.KNIGHT));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, moveToPosition, PieceType.KNIGHT));
                    }
                }
                if (myPosition.getRow() != 2){
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (isInBounds(moveToPosition) && !isBlocked(board, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                    moveToPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                    if (isInBounds(moveToPosition) && isBlocked(board, moveToPosition) && canCapture(board, myPosition, moveToPosition)){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                }
                break;
        }
    }


    private void knightMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves){
        int[][] possibleMoves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {-1, 2}, {-1, -2}, {1, 2}, {1, -2}};
        for (int[] possibleMove : possibleMoves){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + possibleMove[0], myPosition.getColumn() + possibleMove [1]);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
    }

    private void kingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves){
        int[][] possibleMoves = {{1,0}, {1,-1}, {1,1}, {0, -1}, {0,1}, {-1, 0}, {-1, -1}, {-1, 1,}};
        for (int[] possibleMove : possibleMoves){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + possibleMove[0], myPosition.getColumn() + possibleMove [1]);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
    }

    private void bishopMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves){
        //Up Right
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
        //Up Right
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
        //Down Right
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
        //Down Left
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
    }

    private void rookMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves){
        //Up
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
        //Down
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
        //Right
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
        //Left
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
            if (isInBounds(moveToPosition) && (!isBlocked(board, moveToPosition) || canCapture(board, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if (canCapture(board, myPosition, moveToPosition)) {
                    break;
                }
            }
            else {
                break;
            }
        }
    }

    private boolean isInBounds(ChessPosition position){
        return 1 <= position.getRow() && position.getRow() <= 8 && 1 <= position.getColumn() && position.getColumn() <=8;
    }

    private boolean isBlocked(ChessBoard board, ChessPosition moveToPosition){
        return board.getPiece(moveToPosition) != null;
    }

    private boolean canCapture(ChessBoard board, ChessPosition myPosition, ChessPosition moveToPosition){
        if (isBlocked(board, moveToPosition)) {
            return board.getPiece(myPosition).getTeamColor() != board.getPiece(moveToPosition).getTeamColor();
        }
        return false;
    }

    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }
}
