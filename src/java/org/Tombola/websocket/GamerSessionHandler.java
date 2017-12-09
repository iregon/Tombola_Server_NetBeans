
package org.Tombola.websocket;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Session;
//import org.example.model.Device;

@ApplicationScoped
public class GamerSessionHandler {
    private final Set<Session> sessions = new HashSet<>();
//    private final Set<Device> devices = new HashSet<>();
    
    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
}