package com.chatclient.business;

import com.chatclient.config.Config;
import com.chatclient.controllers.MainController;
import com.chatclient.headers.impls.ChatHeaderV1;
import com.chatclient.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServiceThread extends Thread {
    private static MainController mainController;
    private static Socket socket;

    public static void setController(MainController controller) {
        mainController = controller;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(
                    Config.get("server.host"),
                    Integer.parseInt(Config.get("server.port"))
            );

            InputStream is = socket.getInputStream();
            while (true) {
                List<Byte> responseList = new ArrayList<>();
                int i = 0;
                int remainingBytes = 32 + 7;
                while (remainingBytes > 0) {
                    int bytesRead = is.read();
                    if (i == 11) {
                        remainingBytes += bytesRead;
                    }
                    responseList.add((byte) bytesRead);
                    i++;
                    remainingBytes--;
                }

                // Converting List to byte[]
                byte[] response = new byte[responseList.size()];
                for (i = 0; i < response.length; i++) {
                    response[i] = responseList.get(i);
                }

                System.out.println();

                // Printing response
                Packet packetClass = new Packet(new ChatHeaderV1());
                String receivedMessage;
                try {
                    receivedMessage = packetClass.disassemble(response);
                } catch (IllegalArgumentException e) {
                    receivedMessage = e.getMessage();
                }

                receive("some sender", receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(String message) throws IOException {
        Packet packetClass = new Packet(new ChatHeaderV1());
        byte[] packet = packetClass.collect(message);

        OutputStream os = socket.getOutputStream();
        os.write(packet);
        os.flush();

        receive("me", message);
    }

    public static void receive(String sender, String message) {
        mainController.refreshChatHistory(sender, message);
    }

    public void close() {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
