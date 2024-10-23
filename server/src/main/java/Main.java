import chess.*;
import server.Server;

public class Main {
    static Server server = new Server();
    public static void main(String[] args) {
        server.run(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}