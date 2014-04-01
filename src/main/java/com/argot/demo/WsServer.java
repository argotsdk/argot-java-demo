package com.argot.demo;


import javax.ejb.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/sockets")
@Singleton
public class WsServer {

    private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    static Runnable emitter = new Runnable() {
        public void run() {
            ByteBuffer data = ByteBuffer.wrap(new byte[]{1, 2, 3});
            for (Session client : clients) {
                try {
                    client.getBasicRemote().sendBinary(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    static {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(emitter, 0, 3, TimeUnit.SECONDS);
    }

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.console().format("Session started: %s%n", session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        switch (message) {
            case "quit":
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game ended"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
        return message;
    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        clients.remove(session);
        System.console().format("Session ended: %s%n", session.getId());
    }
}
