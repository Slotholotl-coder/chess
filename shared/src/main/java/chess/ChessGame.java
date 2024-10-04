package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurn;
    ChessBoard chessBoard;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.chessBoard = new ChessBoard();
        chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        Collection<ChessMove> potentialMoves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : potentialMoves) {
            // Create a temporary board to test the move
            ChessBoard testChessBoard = chessBoard.deepCopy();

            // Make the move on the temporary board
            testMakeMove(testChessBoard, move);

            // Check if the move leaves the player's king in check
            if (!checkCheck(piece.getTeamColor(), testChessBoard)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (chessBoard.getPiece(move.getStartPosition()) == null || chessBoard.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn())
            throw new InvalidMoveException();
        if (!validMoves(move.getStartPosition()).contains(move))
            throw new InvalidMoveException();
        System.out.println(move.getPromotionPiece() == null ? chessBoard.getPiece(move.getStartPosition()).getPieceType() : move.getPromotionPiece());
        testMakeMove(chessBoard, move);
        setTeamTurn(teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return checkCheck(teamColor, chessBoard);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor))
            return false;

        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                ChessPiece chessPiece = chessBoard.board[row][col];
                if (chessPiece == null || chessPiece.getTeamColor() != teamColor)
                    continue;
                Collection<ChessMove> moves = validMoves(new ChessPosition(row + 1, col + 1));
                if (!moves.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    private void testMakeMove(ChessBoard testChessBoard, ChessMove move){
        testChessBoard.addPiece(move.getEndPosition(), new ChessPiece(chessBoard.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece() == null ? chessBoard.getPiece(move.getStartPosition()).getPieceType() : move.getPromotionPiece()));
        testChessBoard.addPiece(move.getStartPosition(), null);
    }

    public boolean checkCheck(TeamColor teamColor, ChessBoard testChessBoard) {
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                ChessPiece chessPiece = testChessBoard.board[x][y];
                if (chessPiece != null && chessPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> moves = chessPiece.pieceMoves(testChessBoard, new ChessPosition(x + 1, y + 1));
                    for(ChessMove move : moves) {
                        ChessPiece possibleKing = testChessBoard.getPiece(move.getEndPosition());
                        if (possibleKing != null && possibleKing.getPieceType() == ChessPiece.PieceType.KING && possibleKing.getTeamColor() == teamColor){
                            return  true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor))
            return false;
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                ChessPiece chessPiece = chessBoard.board[x][y];
                if (chessPiece != null && chessPiece.getTeamColor() == teamColor){
                    Collection<ChessMove> moves = validMoves(new ChessPosition(x + 1, y + 1));
                    if (!moves.isEmpty())
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
