package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    String message;

    String errorMessage;

    ChessGame game;

    boolean gameOver = false;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setGameOver(boolean bool){
        gameOver = bool;
    }

    public boolean getGameOver(){
        return gameOver;
    }

    public String getMessage(){
        return message;
    }

    public void setErrorMessage(String message){
        errorMessage = message;
    }

    public void setChessGame(ChessGame game){
        this.game = game;
    }

    public ChessGame getChessGame(){
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
