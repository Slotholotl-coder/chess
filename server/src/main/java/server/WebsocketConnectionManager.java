package server;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketConnectionManager {
    public final ConcurrentHashMap<Integer, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        try {
            connections.get(gameID).add(session);
        } catch (Exception e) {
            List<Session> newGameSession = new ArrayList<>();
            newGameSession.add(session);
            connections.put(gameID, newGameSession);
        }
        System.out.println(connections.toString());
    }

    public void remove(int gameID, Session session) {
        connections.get(gameID).remove(session);
    }

    public void broadcast(int gameId, Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for (Session c : connections.get(gameId)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}