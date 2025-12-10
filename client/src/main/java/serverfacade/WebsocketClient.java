package serverfacade;

import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;

public class WebsocketClient extends Endpoint {

    Session session;

    WebsocketNotificationHandler websocketNotificationHandler ;

    ServerFacade serverFacade;

    public WebsocketClient(ServerFacade serverFacade) throws Exception {

        this.serverFacade = serverFacade;

        websocketNotificationHandler = new WebsocketNotificationHandler(serverFacade);

        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                websocketNotificationHandler.notify(message);
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
