package serverFacade;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

public class WebsocketNotificationHandler {

    Gson serializer = new Gson();

    ServerFacade serverFacade;

    public WebsocketNotificationHandler(ServerFacade serverFacade){
        this.serverFacade = serverFacade;
    }

    public void notify(String message){
        ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);

        switch (serverMessage.getServerMessageType()){
            case LOAD_GAME -> serverFacade.sendLoadGame(serverMessage.getChessGame());
            case ERROR, NOTIFICATION -> serverFacade.sendPrintNotification(serverMessage.getMessage());
        }
    }

}
