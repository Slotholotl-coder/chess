package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        switch (pieceType) {
            case KING:
                kingMoves(moves, board, myPosition);
                break;
            case QUEEN:
                bishopMoves(moves, board, myPosition);
                rookMoves(moves, board, myPosition);
                break;
            case BISHOP:
                break;
            case KNIGHT:
                knightMoves(moves, board, myPosition);
                break;
            case ROOK:
                rookMoves(moves, board, myPosition);
                break;
            case PAWN:
                pawnMoves(moves, board, myPosition);
                break;
        }
        return moves;
    }

    private boolean isBlocked(ChessBoard chessBoard, ChessPosition myPosition, ChessPosition moveToPosition) {
        boolean spotTaken = chessBoard.getPiece(moveToPosition) != null;
        if (spotTaken) {
            if (canCapture(chessBoard, moveToPosition, myPosition)) {
                return false;
            }
            else
                return true;
        }
        else
            return spotTaken;
    }

    private boolean canCapture(ChessBoard board, ChessPosition moveToPosition, ChessPosition myPosition) {
        return board.getPiece(myPosition).getTeamColor() != board.getPiece(moveToPosition).getTeamColor();
    }

    private void kingMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        //                  Right   up     left    down   rightUp rightDwn leftUp leftDown
        int[][] kingMoves = {{1,0},{0,1}, {-1,0}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
        for (int[] move : kingMoves) {
            if (myPosition.getRow() + move[0] < 8 && myPosition.getColumn() + move[1] < 8 && !isBlocked(board, myPosition, new ChessPosition(move[0],move[1]))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    private  void pawnMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        if(myPosition.getColumn() + 1 < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1))) {
            ChessPosition forwardMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            moves.add(new ChessMove(myPosition, forwardMove, null));
        }
        if(board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn() + 1)) != null && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1))) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
        }
        if(board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn() - 1)) != null && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1))) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
        }
    }

    private  void knightMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        //                      Right           Left            up              down
        int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2},{-1,2}, {1,-2}, {-1,-2}};
        for (int[] move : knightMoves) {
            if(myPosition.getRow() + move[0] < 8 && myPosition.getColumn() + move[1] < 8 && !isBlocked(board, myPosition, new ChessPosition(move[0],move[1]))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    private  void  rookMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int[] xMoves = new int[8];
        int[] yMoves = new int[8];
        //Positve x
        for (int i = 0; i < 8; i++) {
            if (myPosition.getRow() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        //Negative x
        for (int i = 0; i > -8; i--) {
            if (myPosition.getRow() + i > 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        //Positive y
        for (int i = 0; i < 8; i++) {
            if (myPosition.getColumn() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        //Negative y
        for (int i = 0; i > -8; i--) {
            if (myPosition.getRow() + i > 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    private  void bishopMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        for (int i = 0; i < 8; i++) {
            if (myPosition.getRow() + i < 8 && myPosition.getColumn() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        for (int i = 0; i > -8; i--) {
            if (myPosition.getRow() + i > 8 && myPosition.getColumn() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }
}
