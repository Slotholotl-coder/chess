import chess.*;
import serverFacade.ServerFacade;
import ui.BoardPrinter;
import ui.PreLoginUI;
import server.Server;

public class Main {
    public static void main(String[] args) {

        BoardPrinter boardPrinter = new BoardPrinter();

        boardPrinter.printBoard(new ChessGame(), ChessGame.TeamColor.BLACK);
        System.out.println();
        boardPrinter.printBoard(new ChessGame(), ChessGame.TeamColor.WHITE);

        ServerFacade serverFacade = new ServerFacade("localhost:8080");
        Server server = new Server();
        server.run(8080);
        PreLoginUI preLoginUI = new PreLoginUI(serverFacade);
        preLoginUI.run();
    }
}