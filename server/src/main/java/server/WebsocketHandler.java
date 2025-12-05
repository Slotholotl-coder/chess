package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    Gson serializer = new Gson();

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        System.out.println("Connected to client");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        UserGameCommand command = serializer.fromJson(wsMessageContext.message(), UserGameCommand.class);
        wsMessageContext.send(command.getCommandType());
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
    }

}
