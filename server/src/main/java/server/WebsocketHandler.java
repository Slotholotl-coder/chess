package server;

import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {


    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        System.out.println("Connected to client");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        wsMessageContext.send(wsMessageContext.message());
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
    }

}
