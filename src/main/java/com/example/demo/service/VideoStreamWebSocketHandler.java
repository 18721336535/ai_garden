package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.example.demo.controller.UdpVideoServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// Socket-Connection Configuration class
public class VideoStreamWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LogManager.getLogger(VideoStreamWebSocketHandler.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5); // Increase thread pool size

    public static ApplicationContext applicationContext;

    private UdpVideoServer udpVideoServer;

    byte[] byteArray = new byte[3000];

    // In this list all the connections will be stored
    // Then it will be used to broadcast the message
    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());

    // Map to store ScheduledFuture for each session
    Map<WebSocketSession, ScheduledFuture<?>> sessionTasks = Collections.synchronizedMap(new HashMap<>());

    public static void setApplicationContext(ApplicationContext applicationContext) {
        VideoStreamWebSocketHandler.applicationContext = applicationContext;
    }

    // This method is executed when client tries to connect
    // to the sockets
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        // Logging the connection ID with Connected Message
        logger.info(session.getId() + " Connected");

        udpVideoServer = applicationContext.getBean(UdpVideoServer.class);

        // Adding the session into the list
        webSocketSessions.add(session);

        // Start the scheduler to send frames at 10 FPS
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                byte[] frame = udpVideoServer.getLatestFrame();
//                byte[] frame = byteArray;
                if (session.isOpen()) {
                    session.sendMessage(new BinaryMessage(frame));
                } else {
                    logger.info(session.getId() + " Session is closed, canceling task.");
                    cancelTask(session); // Cancel the task for this session, important to avoid memory leaks
                }
            } catch (IOException e) {
                logger.info("Error sending frame to session " + session.getId() + ": " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                logger.info("Unexpected error in scheduler task for session " + session.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, 300, TimeUnit.MILLISECONDS); // FPS

        // Store the ScheduledFuture for this session
        sessionTasks.put(session, future);
    }

    // When client disconnect from WebSocket then this
    // method is called
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info(session.getId() + " DisConnected");

        // Removing the connection info from the list
        webSocketSessions.remove(session);

        // Cancel the scheduled task for this session
        cancelTask(session);
    }

    // It will handle exchanging of message in the network
    // It will have a session info who is sending the
    // message Also the Message object passes as parameter
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);

        // No need to start a scheduler here
    }

    // Method to cancel the scheduled task for a session
    private void cancelTask(WebSocketSession session) {
        ScheduledFuture<?> future = sessionTasks.get(session);
        if (future != null) {
            future.cancel(false);
            sessionTasks.remove(session);
        }
    }
}
