import chess.ChessGame;
import chess.ChessPiece;
import server.Server;

public class Main {
    static Server server = new Server();

    public static void main(String[] args) {
        server.run(3060);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}