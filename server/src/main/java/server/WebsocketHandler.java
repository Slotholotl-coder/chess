package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    WebsocketConnectionManager websocketConnectionManager = new WebsocketConnectionManager();

    private final Gson serializer = new Gson();

    GameDAO gameDAO;

    public WebsocketHandler(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }

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
                case MAKE_MOVE -> makeMove(session, wsMessageContext);
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
            if (userGameCommand.getTeamColor() != null || userGameCommand.getTeamColor().isEmpty()) {
                serverMessage.setMessage(userGameCommand.getUsername() + " joined the game as " + userGameCommand.getTeamColor());
            } else {
                serverMessage.setMessage(userGameCommand.getUsername() + " is observing");
            }
            websocketConnectionManager.broadcast(userGameCommand.getGameID(), wsMessageContext.session, serverMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void leave(UserGameCommand userGameCommand, Session session, WsMessageContext wsMessageContext){
        try {
            websocketConnectionManager.remove(userGameCommand.getGameID(), session);
            GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
            ChessGame.TeamColor teamColor = getTeamColor(userGameCommand);
            GameData updatedGameData;
            if (teamColor == ChessGame.TeamColor.BLACK){
                updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            } else {
                updatedGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            }

            gameDAO.updateGame(updatedGameData);

            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setMessage(userGameCommand.getUsername() + " left");
            websocketConnectionManager.broadcast(userGameCommand.getGameID(), wsMessageContext.session, serverMessage);
            wsMessageContext.closeSession();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {

        }
    }

    private static ChessGame.TeamColor getTeamColor(String teamColor) {
        return Objects.equals(teamColor, "black") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    private void makeMove(Session session, WsMessageContext wsMessageContext) throws IOException {
        MakeMoveCommand makeMoveCommand = serializer.fromJson(wsMessageContext.message(), MakeMoveCommand.class);
        try {

            GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
            gameData.game().makeMove(makeMoveCommand.getChessMove());

            gameDAO.updateGame(gameData);

            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            String message = makeMoveCommand.getUsername() + makeMoveCommand.getChessMove().toString();
            ChessGame.TeamColor oppositeTeamColor = makeMoveCommand.getTeamColor() == "black" ? ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK;
            if (gameData.game().isInCheckmate(oppositeTeamColor)){
                message += "\n" + oppositeTeamColor + " is in checkmate.\nGood game!";
            } else if (gameData.game().isInCheck(oppositeTeamColor)) {
                message += "\n" + oppositeTeamColor + " is in check";
            }
            serverMessage.setMessage(message);
            serverMessage.setChessGame(gameData.game());
            websocketConnectionManager.broadcast(makeMoveCommand.getGameID(), session, serverMessage);
        } catch (InvalidMoveException | DataAccessException e){
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setMessage("Invalid move");
            wsMessageContext.send(serializer.toJson(serverMessage, ServerMessage.class));
        }
    }


}
