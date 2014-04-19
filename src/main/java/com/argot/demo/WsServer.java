package com.argot.demo;


import javax.ejb.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/socket")
@Singleton
public class WsServer {

  private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

  private static final byte[] dataWithDefinitions = new byte[]{65, 19, 1, 50, 32, 0, 4, 100, 97, 116, 97, 1, 0, 27, 15, 3, 14, 5, 115, 104, 111, 114, 116, 13, 40, 14, 4, 98, 121, 116, 101, 13, 1, 14, 4, 116, 101, 120, 116, 13, 8, 50, 0, 0, 50, 5, 104, 101, 108, 108, 111};

  private static final byte[] simpleData = new byte[]{50, 0, 0, 50, 5, 104, 101, 108, 108, 111};



  static Runnable emitter = new Runnable() {
    int i = 0;
    public void run() {
      ByteBuffer data = ByteBuffer.wrap(getSinePointData(i++));
      for (Session client : clients) {
        try {
          client.getBasicRemote().sendBinary(data);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  };

  private static byte[] getSinePointData(double i) {
    byte[] bytes = Arrays.copyOf(simpleData, simpleData.length);
//    var sin = Math.sin((y++)/50);
    double sin = Math.sin(i / 50);
    int sinePoint = (int) ((sin * 100) + 100);
    bytes[2] = (byte)sinePoint;
    return bytes;
  }


  static {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(emitter, 0, 20, TimeUnit.MILLISECONDS);
  }

  @OnOpen
  public void onOpen(Session session) {
    try {
      session.getBasicRemote().sendBinary(ByteBuffer.wrap(dataWithDefinitions));
      clients.add(session);
      System.console().format("Session started: %s%n", session.getId());
    } catch (IOException e) {
      System.console().format("Could not initialize client %s%n", session.getId());
    }

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
