package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn = TeamColor.WHITE;
    ChessBoard chessBoard = new ChessBoard();

    public ChessGame() {

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
        if (piece == null){
            return null;
        }
        TeamColor pieceColor = piece.getTeamColor();

        Collection<ChessMove> potentialMoves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : potentialMoves){
            ChessBoard tempBoard = chessBoard.deepCopy();

            executeMove(tempBoard, move);

            System.out.println(tempBoard.toString());

            if (!isInCheck(tempBoard, pieceColor)){
                validMoves.add(move);
            }

        }
        return validMoves;
    }

    private void executeMove(ChessBoard board, ChessMove move) {
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        board.addPiece(move.getEndPosition(), promotionPiece == null ? movingPiece : new ChessPiece(movingPiece.getTeamColor(), promotionPiece));
        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (chessBoard.getPiece(move.getStartPosition()) == null || chessBoard.getPiece(move.getStartPosition()).getTeamColor() != teamTurn){
            throw new InvalidMoveException("Not your turn");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (!validMoves.contains(move)){
            throw new InvalidMoveException("Not a valid move");
        }

        executeMove(chessBoard, move);

        TeamColor nextTeam = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;

        setTeamTurn(nextTeam);
    }

    private Collection<Collection<ChessMove>> parseTeamMoves (ChessBoard board, TeamColor team){
        Collection<Collection<ChessMove>> teamsMoves = new ArrayList<>();

        for (int y = 1; y <= 8; y++){
            for (int x = 1; x <= 8; x++){
                ChessPiece chessPiece = board.getPiece(new ChessPosition(y, x) );
                if (chessPiece == null || chessPiece.getTeamColor() != team){
                    continue;
                }
                Collection<ChessMove> pieceMoves = chessPiece.pieceMoves(board, new ChessPosition(y, x));
                teamsMoves.add(pieceMoves);
            }
        }
        return teamsMoves;
    }

    private boolean hasValidMoves(TeamColor teamColor){
        Collection<Collection<ChessMove>> teamsMoves = parseTeamMoves(chessBoard, teamColor);
        return !teamsMoves.isEmpty();
    }

    private boolean isInCheck(ChessBoard board, TeamColor teamColor) {
        TeamColor oppositeTeam = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<Collection<ChessMove>> teamsMoves = parseTeamMoves(board, oppositeTeam);
        for (Collection<ChessMove> pieceMoves : teamsMoves){
            for (ChessMove move : pieceMoves){
                ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                if (capturedPiece == null){
                    continue;
                }
                if (capturedPiece.getPieceType() == ChessPiece.PieceType.KING){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(chessBoard, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return teamColor == teamTurn && isInCheck(teamColor) && !hasValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return teamColor == teamTurn && !isInCheck(teamColor) && !hasValidMoves(teamColor);
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(chessBoard, chessGame.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, chessBoard);
    }
}