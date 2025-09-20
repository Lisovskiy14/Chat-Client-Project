package com.chatclient;

import com.chatclient.config.Config;
import com.chatclient.headers.impls.ChatHeaderV1;
import com.chatclient.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();

            try (Socket socket = new Socket(
                    Config.get("server.host"),
                    Integer.parseInt(Config.get("server.port")))
            ) {

                Packet packetClass = new Packet(new ChatHeaderV1());
                byte[] packet = packetClass.collect(message);

                printDetails(packet);

                OutputStream os = socket.getOutputStream();
                os.write(packet);
                os.flush();

                // Writing response to List
                InputStream is = socket.getInputStream();
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
                printDetails(response);
                String receivedMessage;
                try {
                    receivedMessage = packetClass.disassemble(response);
                } catch (IllegalArgumentException e) {
                    receivedMessage = e.getMessage();
                }
                System.out.println("Received message: " + receivedMessage);

            } catch (UnknownHostException e) {
                System.out.println("Unknown host: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("I/O error: " + e.getMessage());
            }
            scanner.close();
        }
    }

    private static void printDetails(byte[] packet) {
        int payloadLength = packet[11];
        for (int i = 0; i < packet.length; i++) {
            if (i == 0) {
                System.out.print("\nHEADER: ");
            } else if (i == 32) {
                System.out.print("\nPAYLOAD: ");
            } else if (i == 32 + payloadLength) {
                System.out.print("\nEOT: ");
            }
            System.out.print(packet[i] + " ");
        }
        System.out.println();
    }
}
