package com.example.demo.controller;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Component
public class UdpVideoServer {
    private static final Logger logger = LogManager.getLogger(UdpVideoServer.class);
    private byte[] frameBytes = new byte[200000];
    private int frameLength = 0;
    private DatagramSocket socket;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            try {
                socket = new DatagramSocket(8000);
                byte[] buffer = new byte[200000];

                while (true) {

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    int len = packet.getLength();
                    byte[] data = packet.getData();
//                    logger.info("receive a new package size : "+len);
                    synchronized (this) {
                        if (!isValidJpeg(data)) {
                            System.arraycopy(data, 0, frameBytes, frameLength, len);
                            frameLength += len;
                            continue;
                        }
                        frameLength = 0;
                        System.arraycopy(data, 0, frameBytes, frameLength, len);
                        frameLength = len;
                    }
                }
            } catch (Exception e) {
                logger.info("Error in UdpVideoServer: " + e.getMessage());
                closeSocket();
            }
        }).start();
    }

    public synchronized byte[] getLatestFrame() {
        byte[] byteArray = new byte[frameLength];
        System.arraycopy(frameBytes, 0, byteArray, 0, frameLength);
        return byteArray;
    }

    private boolean isValidJpeg(byte[] data) {
        return data.length > 0 &&
                (data[0] & 0xFF) == 0xFF &&
                (data[1] & 0xFF) == 0xD8;
    }

    private void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
