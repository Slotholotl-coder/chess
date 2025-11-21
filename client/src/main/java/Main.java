import chess.*;
import serverFacade.ServerFacade;
import ui.BoardPrinter;
import ui.PreLoginUI;
import server.Server;

public class Main {
    public static void main(String[] args) {
        ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
        Server server = new Server();
        server.run(8080);
        PreLoginUI preLoginUI = new PreLoginUI(serverFacade);
        preLoginUI.run();
    }
}