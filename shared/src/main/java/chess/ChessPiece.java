package chess;

import java.util.ArrayList;
import java.util.Collection;
<<<<<<< HEAD
=======
import java.util.Objects;
>>>>>>> 77252ce (6)

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    PieceType pieceType;

<<<<<<< HEAD
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;
=======
    int directionInt;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;
        this.directionInt = 0;
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> 77252ce (6)
=======

>>>>>>> 106c8cf (6)
=======
>>>>>>> 7295645 (6)
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
<<<<<<< HEAD
=======
                bishopMoves(moves, board, myPosition);
>>>>>>> 77252ce (6)
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
<<<<<<< HEAD

    private boolean isBlocked(ChessBoard chessBoard, ChessPosition myPosition, ChessPosition moveToPosition) {
        boolean spotTaken = chessBoard.getPiece(moveToPosition) != null;
        if (spotTaken) {
            if (canCapture(chessBoard, moveToPosition, myPosition)) {
=======
    private void kingMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        int[][] kingMoves = {{1,0},{0,1}, {-1,0}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
        for (int[] move : kingMoves){
            ChessPosition moveToPosition = new ChessPosition(move[0], move[1]);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
   }

   private  void rookMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        for(int x = 0; x < 8; x++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + x, myPosition.getColumn());
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }

            moveToPosition = new ChessPosition(myPosition.getRow() - x, myPosition.getColumn());
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
        for (int y = 0; y < 8; y++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + y);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }

            moveToPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - y);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
   }
   private void bishopMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        //Up Right
        for(int i = 0; moveInBounds(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i)); i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
        //Down Left
       for(int i = 0; moveInBounds(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i)); i++){
           ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
           if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
               moves.add(new ChessMove(myPosition, moveToPosition, null));
           }
       }
       //Down Right
       for(int i = 0; moveInBounds(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i)); i++){
           ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
           if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
               moves.add(new ChessMove(myPosition, moveToPosition, null));
           }
       }
       for(int i = 0; moveInBounds(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i)); i++){
           ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
           if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
               moves.add(new ChessMove(myPosition, moveToPosition, null));
           }
       }
   }

   private void  knightMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        //                   Rightup  Rightdwn Leftup  Leftdwn Upright upleft Dwnright  downleft
        int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2},{-1,2}, {1,-2}, {-1,-2}};
        for (int[] move : knightMoves) {
            if(myPosition.getRow() + move[0] < 8 && myPosition.getColumn() + move[1] < 8 && !isBlocked(chessBoard, myPosition, new ChessPosition(move[0],move[1]))) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
   }

   private void pawnMoves (Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        switch (pieceColor){
            case WHITE:
                directionInt = 1;
                if(myPosition.getRow() == 2){
                    if(moveInBounds(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 2))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + directionInt), null));
                    }
                }
                break;
            case BLACK:
                directionInt = -1;
                if(myPosition.getRow() == 7){
                    if(moveInBounds(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 2))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + directionInt), null));
                    }
                }
                break;
        }
        if(moveInBounds(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + directionInt))){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + directionInt), null));
        }

        //Capture moves
        ChessPosition rightDiagonal = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + directionInt);
        ChessPosition leftDiagonal = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + directionInt);

        if(moveInBounds(rightDiagonal) && canCapture(chessBoard, myPosition, rightDiagonal)){
            moves.add(new ChessMove(myPosition, rightDiagonal, null));
        }
        if(moveInBounds(leftDiagonal) && canCapture(chessBoard, myPosition, leftDiagonal)){
            moves.add(new ChessMove(myPosition, leftDiagonal, null));
        }
   }

   private boolean moveInBounds(ChessPosition position){
        boolean inXBounds = position.getRow() < 8 && position.getRow() > 1;
        boolean inYBounds = position.getColumn() < 8 && position.getColumn() > 1;
        return inXBounds && inYBounds;
   }
    private boolean isBlocked(ChessBoard chessBoard, ChessPosition myPosition, ChessPosition moveToPosition) {
        boolean spotTaken = chessBoard.getPiece(moveToPosition) != null;
        if (spotTaken) {
            if (canCapture(chessBoard, myPosition, moveToPosition)) {
>>>>>>> 77252ce (6)
                return false;
            }
            else
                return true;
        }
        else
            return spotTaken;
    }

<<<<<<< HEAD
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
=======
    private boolean canCapture(ChessBoard board, ChessPosition myPosition, ChessPosition moveToPosition) {
        return board.getPiece(myPosition).getTeamColor() != board.getPiece(moveToPosition).getTeamColor();
    }




//    private void kingMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
//        //                  Right   up     left    down   rightUp rightDwn leftUp leftDown
//        int[][] kingMoves = {{1,0},{0,1}, {-1,0}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
//        for (int[] move : kingMoves) {
//            if (myPosition.getRow() + move[0] < 8 && myPosition.getColumn() + move[1] < 8 && !isBlocked(board, myPosition, new ChessPosition(move[0],move[1]))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//    }
//
//    private  void pawnMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
//        if(myPosition.getColumn() + 1 < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1))) {
//            ChessPosition forwardMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
//            moves.add(new ChessMove(myPosition, forwardMove, null));
//        }
//        if(board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn() + 1)) != null && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1))) {
//            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
//        }
//        if(board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn() - 1)) != null && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1))) {
//            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
//        }
//    }
//
//    private  void knightMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
//        //                      Right           Left            up              down
//        int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2},{-1,2}, {1,-2}, {-1,-2}};
//        for (int[] move : knightMoves) {
//            if(myPosition.getRow() + move[0] < 8 && myPosition.getColumn() + move[1] < 8 && !isBlocked(board, myPosition, new ChessPosition(move[0],move[1]))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//    }
//
//    private  void  rookMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
//        int[] xMoves = new int[8];
//        int[] yMoves = new int[8];
//        //Positve x
//        for (int i = 0; i < 8; i++) {
//            if (myPosition.getRow() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//        //Negative x
//        for (int i = 0; i > -8; i--) {
//            if (myPosition.getRow() + i > 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//        //Positive y
//        for (int i = 0; i < 8; i++) {
//            if (myPosition.getColumn() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//        //Negative y
//        for (int i = 0; i > -8; i--) {
//            if (myPosition.getRow() + i > 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//    }
//
//    private  void bishopMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
//        for (int i = 0; i < 8; i++) {
//            if (myPosition.getRow() + i < 8 && myPosition.getColumn() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//        for (int i = 0; i > -8; i--) {
//            if (myPosition.getRow() + i > 8 && myPosition.getColumn() + i < 8 && !isBlocked(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i))) {
//                ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
//                moves.add(new ChessMove(myPosition, newPosition, null));
//            }
//        }
//    }


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
>>>>>>> 77252ce (6)
    }
}
