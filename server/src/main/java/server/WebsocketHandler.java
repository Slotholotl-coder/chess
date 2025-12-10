package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import model.AuthData;
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
    AuthDAO authDAO;

    public WebsocketHandler(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
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

            authDAO.getAuthToken(userGameCommand.getAuthToken());

            switch (userGameCommand.getCommandType()){
                case CONNECT -> connect(userGameCommand, session, wsMessageContext);
                case LEAVE -> leave(userGameCommand, session, wsMessageContext);
                case MAKE_MOVE -> makeMove(session, wsMessageContext);
                case RESIGN -> resign(userGameCommand);
            }
        } catch(Exception e){
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("error " + e.getMessage());
            wsMessageContext.send(serializer.toJson(errorMessage, ServerMessage.class));
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected");
    }

    private void resign(UserGameCommand userGameCommand) throws IOException, DataAccessException {
        GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
        if (gameData.game().isGameOver() || (
                !Objects.equals(authDAO.getAuthToken(userGameCommand.getAuthToken()).username(), gameData.blackUsername()) &&
                !Objects.equals(authDAO.getAuthToken(userGameCommand.getAuthToken()).username(), gameData.whiteUsername()))){
            throw new DataAccessException("You can't resign");
        }
        gameData.game().setGameOver(true);
        gameDAO.updateGame(gameData);

        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        serverMessage.setMessage(userGameCommand.getUsername() + " resigned");
        serverMessage.setGameOver(true);
        websocketConnectionManager.broadcast(userGameCommand.getGameID(), null, serverMessage);
    }

    private void connect(UserGameCommand userGameCommand, Session session,
                         WsMessageContext wsMessageContext) throws DataAccessException, IOException {
        websocketConnectionManager.add(userGameCommand.getGameID(), session);
        ServerMessage loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGame.setChessGame(gameDAO.getGame(userGameCommand.getGameID()).game());
        wsMessageContext.send(serializer.toJson(loadGame, ServerMessage.class));

        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        if (userGameCommand.getTeamColor() != null) {
            serverMessage.setMessage(userGameCommand.getUsername() + " joined the game as " + userGameCommand.getTeamColor());
        } else {
            serverMessage.setMessage(userGameCommand.getUsername() + " is observing");
        }
        websocketConnectionManager.broadcast(userGameCommand.getGameID(), wsMessageContext.session, serverMessage);
    }

    private void leave(UserGameCommand userGameCommand, Session session, WsMessageContext wsMessageContext) throws DataAccessException, IOException {
        GameData gameData = gameDAO.getGame(userGameCommand.getGameID());
        ChessGame.TeamColor teamColor = getTeamColor(userGameCommand.getTeamColor());
        GameData updatedGameData;
        if (teamColor == ChessGame.TeamColor.BLACK){
            updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        } else {
            updatedGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }

        gameDAO.updateGame(updatedGameData);

        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        serverMessage.setMessage(userGameCommand.getUsername() + " left");
        websocketConnectionManager.broadcast(userGameCommand.getGameID(), session, serverMessage);
        wsMessageContext.closeSession();
        websocketConnectionManager.remove(userGameCommand.getGameID(), session);
    }

    private static ChessGame.TeamColor getTeamColor(String teamColor) {
        return Objects.equals(teamColor, "BLACK") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    private void makeMove(Session session, WsMessageContext wsMessageContext) throws IOException {
        MakeMoveCommand makeMoveCommand = serializer.fromJson(wsMessageContext.message(), MakeMoveCommand.class);
        try {

            GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
            ChessGame.TeamColor teamColor = gameDAO.getGame(makeMoveCommand.getGameID()).game().getTeamTurn();
            if (gameData.game().isGameOver() ||
                    !Objects.equals(authDAO.getAuthToken(makeMoveCommand.getAuthToken()).username(),
                    teamColor == ChessGame.TeamColor.BLACK ? gameData.blackUsername() : gameData.whiteUsername())){
                throw new DataAccessException("Not your turn");
            }

            gameData.game().makeMove(makeMoveCommand.getChessMove());

            gameDAO.updateGame(gameData);

            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            serverMessage.setMessage(makeMoveCommand.getUsername() + " " + makeMoveCommand.getChessMove().toString());
            websocketConnectionManager.broadcast(makeMoveCommand.getGameID(), session, serverMessage);

            ServerMessage updateGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            updateGameMessage.setChessGame(gameData.game());



            ServerMessage checkMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);

            ChessGame.TeamColor oppositeTeamColor = Objects.equals(makeMoveCommand.getTeamColor(), "BLACK") ?
                    ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            if (gameData.game().isInCheckmate(oppositeTeamColor)){
                checkMessage.setMessage(oppositeTeamColor + " is in checkmate.\nGood game!");
                updateGameMessage.setGameOver(true);
            } else if (gameData.game().isInCheck(oppositeTeamColor)) {
                checkMessage.setMessage(oppositeTeamColor + " is in check");
                updateGameMessage.setGameOver(true);
            } else if (gameData.game().isInStalemate(oppositeTeamColor)) {
                checkMessage.setMessage(oppositeTeamColor + " is in stalemate.\nGood game!");
                updateGameMessage.setGameOver(true);
            }
            if (checkMessage.getMessage() != null) {
                websocketConnectionManager.broadcast(makeMoveCommand.getGameID(), null, checkMessage);
            }

            websocketConnectionManager.broadcast(makeMoveCommand.getGameID(), null, updateGameMessage);

        } catch (InvalidMoveException | DataAccessException e){
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            serverMessage.setErrorMessage(e.getMessage() == null ? "Invalid move" : e.getMessage());
            wsMessageContext.send(serializer.toJson(serverMessage, ServerMessage.class));
        }
    }


}
