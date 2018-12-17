import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value="/chat")
public class ChatServerEndPoint {

    private Set<Session> sessionSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * Callback hook for Connection open events.
     * <p>
     * This method will be invoked when a client requests for
     * a WebSocket connection.
     *
     * @param session the user session which is opened.
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New request received. Id: " + session.getId());
        sessionSet.add(session);
    }

    /**
     * Callback hook for Connection close events.
     *
     * This method will be invoked when a client closes a WebSocket
     * connection.
     *
     * @param session the user session which is opened.
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed, Id: " + session.getId());
        sessionSet.remove(session);
    }

    /**
     * Callback hook for Message Events.
     *
     * This method will be invoked when a client sends a message.
     *
     * @param message the text message.
     * @param session the session of the client.
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message Received: " + message);
        for (Session userSession : sessionSet) {
            System.out.println("Sending to " + userSession.getId());
            userSession.getAsyncRemote().sendText(message);
        }
    }
}
