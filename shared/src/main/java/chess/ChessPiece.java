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
    ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
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
        switch (pieceType) {
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

    private void pawnMoves(ChessBoard chessBoard, ChessPosition myPosition, Collection<ChessMove> moves){
        int direction = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int firstRow = pieceColor == ChessGame.TeamColor.WHITE ? 2 : 7;
        int promotionNextRow = pieceColor == ChessGame.TeamColor.WHITE ? 2 : 7;

        ChessPosition moveToPosition = new ChessPosition(0, 0);

        if (myPosition.getRow() != promotionNextRow){
            moveToPosition = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, false);
        }
        else {
            moveToPosition = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, PieceType.QUEEN, false);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, PieceType.ROOK, false);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, PieceType.BISHOP, false);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, PieceType.KNIGHT, false);

        }
        if (myPosition.getRow() == firstRow){
            moveToPosition = new ChessPosition(myPosition.getRow() + 2*direction, myPosition.getColumn());
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, false);
        }


    }

    private void bishopMoves(ChessBoard chessBoard, ChessPosition myPosition, Collection<ChessMove> moves){
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
    }

    private void rookMoves(ChessBoard chessBoard, ChessPosition myPosition, Collection<ChessMove> moves){
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
        for (int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
            attemptToAddMove(chessBoard, myPosition, moveToPosition, moves, null, true);
            if (!isInBounds(moveToPosition) || isBlocked(chessBoard, moveToPosition)){
                break;
            }
        }
    }

    private void kingMoves(ChessBoard chessBoard, ChessPosition myPosition, Collection<ChessMove> moves){
        int[][] possibleMoves = {{1, 1}, {1, 0}, {1, -1,}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {0, 1}};

        for (int[] move : possibleMoves){
            ChessPosition moveToPositon = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
            attemptToAddMove(chessBoard, myPosition, moveToPositon, moves, null, true);
        }

    }

    private void knightMoves(ChessBoard chessBoard, ChessPosition myPosition, Collection<ChessMove> moves){
        int[][] possibleMoves = {{2, 1}, {2, -1,}, {-2, 1}, {-2, -1}, {1, -2}, {1, 2}, {-1, -2}, {-1, 2}};

        for (int[] move : possibleMoves){
            ChessPosition moveToPositon = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
            attemptToAddMove(chessBoard, myPosition, moveToPositon, moves, null, true);
        }

    }

    private void attemptToAddMove(ChessBoard chessBoard, ChessPosition myPosition, ChessPosition moveToPosition, Collection<ChessMove> moves, ChessPiece.PieceType promotionPiece, boolean checkCanCapture){
        if (checkCanCapture){
            if (isInBounds(moveToPosition) && (!isBlocked(chessBoard, moveToPosition) || canCapture(chessBoard, myPosition, moveToPosition))){
                moves.add(new ChessMove(myPosition, moveToPosition, promotionPiece));
            }
        }
        else {
            if (isInBounds(moveToPosition) && !isBlocked(chessBoard, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, promotionPiece));
            }
        }
    }

    private boolean canCapture(ChessBoard chessBoard, ChessPosition myPosition, ChessPosition moveToPosition){
        return chessBoard.getPiece(myPosition).getTeamColor() != chessBoard.getPiece(moveToPosition).getTeamColor();
    }

    private boolean isBlocked(ChessBoard chessBoard, ChessPosition moveToPosition){
        return chessBoard.getPiece(moveToPosition) != null;
    }

    private boolean isInBounds(ChessPosition position){
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
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