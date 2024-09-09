package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

//9/7

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    PieceType pieceType;

    int directionInt;

    ChessBoard chessBoard;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;
        this.directionInt = 0;
        this.chessBoard = null;
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
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        chessBoard = chessBoard;
        switch (pieceType) {
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
            case KNIGHT:
                knightMoves(moves, chessBoard, myPosition);
                break;
            case ROOK:
                rookMoves(moves, chessBoard, myPosition);
                break;
            case PAWN:
                pawnMoves(moves, chessBoard, myPosition);
                break;
        }
        return moves;
    }
    private void kingMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        int[][] kingMoves = {{1,0},{0,1}, {-1,0}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
        for (int[] move : kingMoves){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
        }
    }

    private  void rookMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        //Right
        for(int i = 1; i<=8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() , myPosition.getColumn() + i);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        //Left
        for(int i = 1; i<=8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() , myPosition.getColumn() - i);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        //Up
        for(int i = 1; i<=8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() );
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        //Down
        for(int i = 1; i<=8; i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() );
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        for(ChessMove move : moves){
            //System.out.println(move.startPosition.getColumn() +1 + "," + (move.startPosition.getRow() + 1));
            System.out.println(((move.endPosition.getColumn() + 1) + "," + (move.endPosition.getRow() + 1)));
        }
    }
    private void bishopMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        //Up Right
        for(int i = 1; moveInBounds(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i)); i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        //Down Left
        for(int i = 1; moveInBounds(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i)); i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        //Up Left
        for(int i = 1; moveInBounds(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i)); i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        //Down right
        for(int i = 1; moveInBounds(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i)); i++){
            ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            if(moveInBounds(moveToPosition) && !isBlocked(chessBoard, myPosition, moveToPosition)){
                if (chessBoard.getPiece(moveToPosition) != null && canCapture(chessBoard, myPosition, moveToPosition)) {
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
                    break;
                }
                else
                    moves.add(new ChessMove(myPosition, moveToPosition, null));
            }
            else
                break;
        }
        for(ChessMove move : moves){
            //System.out.println(move.startPosition.getColumn() +1 + "," + (move.startPosition.getRow() + 1));
            System.out.println(((move.endPosition.getColumn() + 1) + "," + (move.endPosition.getRow() + 1)));
        }
    }

    private void  knightMoves(Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        //                   Rightup  Rightdwn Leftup  Leftdwn Upright upleft Dwnright  downleft
        int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2},{-1,2}, {1,-2}, {-1,-2}};
        for (int[] move : knightMoves) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + move[0], myPosition.getColumn() + move[1]);
            if(moveInBounds(newPosition) && !isBlocked(chessBoard, myPosition, newPosition)) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }

    private void pawnMoves (Collection<ChessMove> moves, ChessBoard chessBoard, ChessPosition myPosition){
        switch (pieceColor){
            case WHITE:
                directionInt = 1;

                //Move up two for White first-move pawns
                if(myPosition.getRow() == 2){
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    if(chessBoard.getPiece(moveToPosition) == null && chessBoard.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn())) == null){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                }
                break;
            case BLACK:
                directionInt = -1;

                //Move down two for Black first move pawns
                if(myPosition.getRow() == 7){
                    ChessPosition moveToPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if(chessBoard.getPiece(moveToPosition) == null && chessBoard.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn())) == null){
                        moves.add(new ChessMove(myPosition, moveToPosition, null));
                    }
                }
                break;
        }
        ChessPosition forwardMove = new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn());
        if(moveInBounds(forwardMove) && chessBoard.getPiece(forwardMove) == null){
            if (canPromote(chessBoard.getPiece(myPosition).getTeamColor(), forwardMove)) {
                moves.add(new ChessMove(myPosition, forwardMove, PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, forwardMove, PieceType.ROOK));
                moves.add(new ChessMove(myPosition, forwardMove, PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, forwardMove, PieceType.BISHOP));
            }
            else
                moves.add(new ChessMove(myPosition, forwardMove, null));
        }

        //Capture moves
        ChessPosition rightDiagonal = new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn() + 1);
        ChessPosition leftDiagonal = new ChessPosition(myPosition.getRow() + directionInt, myPosition.getColumn() - 1);

        if(moveInBounds(rightDiagonal) && !isBlocked(chessBoard, myPosition, rightDiagonal)){
            if (chessBoard.getPiece(rightDiagonal) != null && canCapture(chessBoard, myPosition, rightDiagonal)) {
                if(canPromote(chessBoard.getPiece(myPosition).getTeamColor(), rightDiagonal)){
                    moves.add(new ChessMove(myPosition, rightDiagonal, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, rightDiagonal, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, rightDiagonal, PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, rightDiagonal, PieceType.BISHOP));
                }
                else
                    moves.add(new ChessMove(myPosition, rightDiagonal, null));
            }
        }
        if(moveInBounds(leftDiagonal) && !isBlocked(chessBoard, myPosition, leftDiagonal)){
            if (chessBoard.getPiece(leftDiagonal) != null && canCapture(chessBoard, myPosition, leftDiagonal)){
                if(canPromote(chessBoard.getPiece(myPosition).getTeamColor(), leftDiagonal)){
                    moves.add(new ChessMove(myPosition, leftDiagonal, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, leftDiagonal, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, leftDiagonal, PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, leftDiagonal, PieceType.BISHOP));
                }
                else
                    moves.add(new ChessMove(myPosition, leftDiagonal, null));
                }
        }

        for(ChessMove move : moves){
            //System.out.println(move.startPosition.getColumn() + "," + (move.startPosition.getRow()));
            System.out.println(((move.endPosition.getColumn()) + "," + (move.endPosition.getRow())));
        }
    }

    private boolean canPromote(ChessGame.TeamColor color, ChessPosition position){
        return (color == ChessGame.TeamColor.WHITE) ? position.getRow() == 8 : position.getRow() == 1;
    }


    private boolean moveInBounds(ChessPosition position){
        boolean inHeightBounds = position.getRow() <= 8 && position.getRow() > 0;
        boolean inWidthBounds = position.getColumn() <= 8 && position.getColumn() > 0;
        return inHeightBounds && inWidthBounds;
    }
    private boolean isBlocked(ChessBoard chessBoard, ChessPosition myPosition, ChessPosition moveToPosition) {
        boolean spotTaken = chessBoard.getPiece(moveToPosition) != null;
        if (spotTaken) {
            if (canCapture(chessBoard, myPosition, moveToPosition)) {
                return false;
            }
            else
                return true;
        }
        else
            return spotTaken;
    }

    private boolean canCapture(ChessBoard board, ChessPosition myPosition, ChessPosition moveToPosition) {
        return board.getPiece(moveToPosition).getTeamColor() != null && board.getPiece(myPosition).getTeamColor() != board.getPiece(moveToPosition).getTeamColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return directionInt == that.directionInt && pieceColor == that.pieceColor && pieceType == that.pieceType && Objects.equals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType, directionInt, chessBoard);
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

}
