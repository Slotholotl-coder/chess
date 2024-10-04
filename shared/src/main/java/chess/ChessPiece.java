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

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch (pieceType){
            case KING:
                kingMoves(moves, chessBoard, myPosition);
                break;
            case QUEEN:
                bishopMoves(moves, chessBoard, myPosition);
                rookMoves(moves, chessBoard, myPosition);
                break;
            case BISHOP:
                bishopMoves(moves, chessBoard, myPosition);
                break;
            case ROOK:
                rookMoves(moves, chessBoard, myPosition);
                break;
            case KNIGHT:
                knightMoves(moves, chessBoard, myPosition);
                break;
            case PAWN:
                pawnMoves(moves, chessBoard, myPosition);
                break;
        }
        return moves;
    }

    private void kingMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        int[][] possibleMoves = {{1,1}, {1,0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1,-1}};
        for(int[] move : possibleMoves){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
            if(validMove(chessBoard, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
    }

    private void bishopMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        for(int i = 1; i <= 8; i++) {
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if (validMove(chessBoard, moveToPosition)) {
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
        for(int i = 1; i <= 8; i++) {
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            if (validMove(chessBoard, moveToPosition)) {
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
        for(int i = 1; i <= 8; i++) {
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            if (validMove(chessBoard, moveToPosition)) {
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
        for(int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if(validMove(chessBoard, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
    }

    private void rookMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        for(int i = 1; i <= 8; i++) {
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() );
            if (validMove(chessBoard, moveToPosition)) {
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
        for(int i = 1; i <= 8; i++) {
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            if (validMove(chessBoard, moveToPosition)) {
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
        for(int i = 1; i <= 8; i++) {
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
            if (validMove(chessBoard, moveToPosition)) {
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
        for(int i = 1; i <= 8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
            if(validMove(chessBoard, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
                if(isBlocked(chessBoard, moveToPosition) && canCapture(chessBoard, moveToPosition)){
                    break;
                }
            }
            else
                break;
        }
    }

    private void knightMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        int[][] possibleMoves = {{2,1}, {2, -1}, {-2, 1}, {-2, -1}, {1, -2}, {-1, -2}, {1, 2}, {-1, 2}};
        for(int[] move : possibleMoves){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
            if(validMove(chessBoard, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
    }

    private void pawnMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        int directionInt = 0;
        switch (pieceColor){
            case WHITE:
                directionInt = 1;

                if(myPosition.getRow() == 2){
                    ChessPosition moveTwo = new ChessPosition(myPosition.getRow() + (2 * directionInt), myPosition.getColumn());
                    if(validMove(chessBoard, moveTwo) && chessBoard.getPiece(new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn())) == null){
                        if(!isBlocked(chessBoard, moveTwo))
                            moves.add(new ChessMove(myPosition, moveTwo, null));
                    }
                }
                break;
            case BLACK:
                directionInt = -1;

                if(myPosition.getRow() == 7){
                    ChessPosition moveTwo = new ChessPosition(myPosition.getRow() + (2 * directionInt), myPosition.getColumn());
                    if(validMove(chessBoard, moveTwo) && chessBoard.getPiece(new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn())) == null){
                        if(!isBlocked(chessBoard, moveTwo))
                            moves.add(new ChessMove(myPosition, moveTwo, null));
                    }
                }
                break;
        }
        ChessPosition forwardMove = new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn());
        if(validMove(chessBoard, forwardMove) && !isBlocked(chessBoard, forwardMove)){
            promotion(moves, myPosition, forwardMove);
        }

        ChessPosition rightDiagonal = new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn() + 1);
        if(isBlocked(chessBoard, rightDiagonal) && canCapture(chessBoard, rightDiagonal) && validMove(chessBoard, rightDiagonal)){
            promotion(moves, myPosition, rightDiagonal);
        }
        ChessPosition leftDiagonal = new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn() - 1);
        if(isBlocked(chessBoard, leftDiagonal) && canCapture(chessBoard, leftDiagonal) && validMove(chessBoard, leftDiagonal)){
            promotion(moves, myPosition, leftDiagonal);
        }
    }

    private void promotion(Collection<ChessMove> moves, ChessPosition myPosition, ChessPosition moveToPosition){
        boolean promote = false;
        switch (pieceColor){
            case WHITE:
                if(moveToPosition.getRow() == 8){
                    promote = true;
                }
                break;
            case BLACK:
                if(moveToPosition.getRow() == 1){
                    promote = true;
                }
                break;
        }
        if(promote){
            moves.add(new ChessMove(myPosition, moveToPosition, PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, moveToPosition, PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, moveToPosition, PieceType.ROOK));
            moves.add(new ChessMove(myPosition, moveToPosition, PieceType.KNIGHT));
        }
        else
            moves.add((new ChessMove(myPosition, moveToPosition, null)));
    }

    private boolean inBounds(ChessPosition moveToPosition){
        return moveToPosition.getRow() <= 8 && moveToPosition.getRow() >= 1 && moveToPosition.getColumn() <= 8 && moveToPosition.getColumn() >= 1;
    }

    private boolean isBlocked(ChessBoard chessBoard, ChessPosition moveToPosition){
        return chessBoard.getPiece(moveToPosition) != null;
    }

    private boolean canCapture(ChessBoard chessBoard, ChessPosition moveToPosition){
        return pieceColor != chessBoard.getPiece(moveToPosition).getTeamColor();
    }

    private boolean validMove(ChessBoard chessBoard, ChessPosition moveToPosition){
        return inBounds(moveToPosition) && (!isBlocked(chessBoard, moveToPosition) || canCapture(chessBoard, moveToPosition));
    }

    @Override
    public String toString() {
        return " " + pieceType + (pieceColor == ChessGame.TeamColor.WHITE ? "W" : "B");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

}
