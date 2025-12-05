package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

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

            switch (userGameCommand.getCommandType()){
                case CONNECT -> connect(userGameCommand.getGameID(), session, wsMessageContext);
                case LEAVE -> leave(userGameCommand.getGameID(), session, wsMessageContext);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
    }

    private void connect(int gameID, Session session, WsMessageContext wsMessageContext){
        websocketConnectionManager.add(gameID, session);
        wsMessageContext.send(serializer.toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME)));

        try {
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setMessage("joined the game");
            websocketConnectionManager.broadcast(gameID, wsMessageContext.session, serverMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void leave(int gameID, Session session, WsMessageContext wsMessageContext){
        try {
            websocketConnectionManager.broadcast(gameID, null, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        websocketConnectionManager.remove(gameID, session);
    }

}
