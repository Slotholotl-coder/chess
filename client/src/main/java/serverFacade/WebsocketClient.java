package serverFacade;

import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class WebsocketClient extends Endpoint {

    Session session;

    WebsocketClient client;

    public void run() throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo:");
        while(true) {
            client.send(scanner.nextLine());
        }
    }

    public WebsocketClient() throws Exception {
        client = this;
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
                System.out.println("\nEnter another message you want to echo:");
            }
        });
    }

    public void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
