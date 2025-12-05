package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    WebsocketConnectionManager websocketConnectionManager = new WebsocketConnectionManager();

    private final Gson serializer = new Gson();

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        System.out.println("Connected to client");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        Session session = wsMessageContext.session;
        try {
            UserGameCommand userGameCommand = serializer.fromJson(wsMessageContext.message(), UserGameCommand.class);
            websocketConnectionManager.add(userGameCommand.gameID(), session);
            wsMessageContext.send(userGameCommand.commandType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
    }

}
