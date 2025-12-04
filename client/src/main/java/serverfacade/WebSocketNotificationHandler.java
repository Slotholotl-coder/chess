package serverFacade;

import websocket.messages.ServerMessage;


public class WebSocketNotificationHandler {

    public void notify(ServerMessage message){
        switch (message.getServerMessageType()){
            case ERROR -> handleError("error");
            case NOTIFICATION -> handleNotification("notification");
            case LOAD_GAME -> handleLoadGame();
        }
    }

    private void handleError(String error){
        System.out.println(error);
    }

    private void handleNotification(String notification){
        System.out.println(notification);
    }

    private void handleLoadGame(){
        System.out.println("load game");
    }

}
