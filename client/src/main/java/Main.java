import serverfacade.ServerFacade;
import ui.PreLoginUI;

public class Main {
    public static void main(String[] args) {
        int port = 8080;

        ServerFacade serverFacade = new ServerFacade(port);

        PreLoginUI preLoginUI = new PreLoginUI(serverFacade);
        preLoginUI.run();
    }
}