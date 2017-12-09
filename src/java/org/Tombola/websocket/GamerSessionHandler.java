
package org.Tombola.websocket;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import org.Tombola.model.Gamer;

@ApplicationScoped
public class GamerSessionHandler {
    private int gamerId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Gamer> gamers = new HashSet<>();
    
    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    public List<Gamer> getGamers() {
        return new ArrayList<>(gamers);
    }

    public void addGamer(Gamer gamer) {
        gamer.setId(gamerId);
        gamers.add(gamer);
        gamerId++;
        JsonObject addMessage = createAddMessage(gamer);
        sendToAllConnectedSessions(addMessage);
    }

    public void removeGamer(int id) {
        Gamer device = getGamerById(id);
        if (device != null) {
            gamers.remove(device);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                .add("action", "remove")
                .add("id", id)
                .build();
            sendToAllConnectedSessions(removeMessage);
        }
    }

    public void toggleGamer(int id) {
        JsonProvider provider = JsonProvider.provider();
        Gamer gamer = getGamerById(id);
        if (gamer != null) {
            if ("On".equals(gamer.getStatus())) {
                gamer.setStatus("Off");
            } else {
                gamer.setStatus("On");
            }
            JsonObject updateDevMessage = provider.createObjectBuilder()
                .add("action", "toggle")
                .add("id", gamer.getId())
                .add("status", gamer.getStatus())
                .build();
            sendToAllConnectedSessions(updateDevMessage);
        }
    }

    private Gamer getGamerById(int id) {
        for (Gamer gamer : gamers) {
            if (gamer.getId() == id) {
                return gamer;
            }
        }
        return null;
    }

    private JsonObject createAddMessage(Gamer gamer) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", gamer.getId())
                .add("name", gamer.getName())
                .add("type", gamer.getType())
                .add("status", gamer.getStatus())
                .build();
        return addMessage;
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(GamerSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}