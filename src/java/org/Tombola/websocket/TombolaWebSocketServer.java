
package org.Tombola.websocket;

import java.io.StringReader;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.*;

@ApplicationScoped
@ServerEndpoint("/actions")
public class TombolaWebSocketServer {
	
	@Inject
    private GamerSessionHandler sessionHandler;
	
	@OnOpen
	public void open(Session session) {
		sessionHandler.addSession(session);
	}
	
	@OnClose
    public void close(Session session) {
		
	}
	
	@OnError
    public void onError(Throwable error) {
            
	}
	
	@OnMessage
	public void handleMessage(String message, Session session) {
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            System.out.println("org.Tombola.websocket.TombolaWebSocketServer.handleMessage()");
            System.out.println(jsonMessage.toString());

//            if ("add".equals(jsonMessage.getString("action"))) {
//                Device device = new Device();
//                device.setName(jsonMessage.getString("name"));
//                device.setDescription(jsonMessage.getString("description"));
//                device.setType(jsonMessage.getString("type"));
//                device.setStatus("Off");
//                sessionHandler.addDevice(device);
//            }
//
//            if ("remove".equals(jsonMessage.getString("action"))) {
//                int id = (int) jsonMessage.getInt("id");
//                sessionHandler.removeDevice(id);
//            }
//
//            if ("toggle".equals(jsonMessage.getString("action"))) {
//                int id = (int) jsonMessage.getInt("id");
//                sessionHandler.toggleDevice(id);
//            }
        }
	}
	
	public static void main(String[] args) {
        new TombolaWebSocketServer();
    }
}
