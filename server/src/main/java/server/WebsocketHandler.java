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
                case CONNECT -> connect(userGameCommand, session, wsMessageContext);
                case LEAVE -> leave(userGameCommand, session, wsMessageContext);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
    }

    private void connect(UserGameCommand userGameCommand, Session session, WsMessageContext wsMessageContext){
        websocketConnectionManager.add(userGameCommand.getGameID(), session);
        wsMessageContext.send(serializer.toJson(new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME), ServerMessage.class));

        try {
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setMessage(userGameCommand.getUsername() + " joined the game as " +userGameCommand.getTeamColor());
            websocketConnectionManager.broadcast(userGameCommand.getGameID(), wsMessageContext.session, serverMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void leave(UserGameCommand userGameCommand, Session session, WsMessageContext wsMessageContext){
        try {
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setMessage(userGameCommand.getUsername() + " left");
            websocketConnectionManager.broadcast(userGameCommand.getGameID(), wsMessageContext.session, serverMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        websocketConnectionManager.remove(userGameCommand.getGameID(), session);
    }

}
