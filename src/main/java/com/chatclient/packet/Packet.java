package com.chatclient.packet;

import com.chatclient.config.Config;
import com.chatclient.headers.Header;

import java.nio.charset.StandardCharsets;

public class Packet {

    private final Header headerClass;

    public Packet(Header headerClass) {
        this.headerClass = headerClass;
    }

    public byte[] collect(String message) {

        // Collecting payload
        byte[] payload = message.getBytes(StandardCharsets.UTF_8);

        // Collecting header
        byte[] header = headerClass.collect(payload.length);

        // Collecting EOT
        byte[] eot = Config.EOT;

        byte[] packet = new byte[header.length + payload.length + eot.length];
        System.arraycopy(header, 0, packet, 0, header.length);
        System.arraycopy(payload, 0, packet, header.length, payload.length);
        System.arraycopy(eot, 0, packet, header.length + payload.length, eot.length);

        return packet;
    }

    public String disassemble(byte[] packet) {
        System.out.println("Trying to disassemble packet...");

        byte[] header = new byte[Config.HEADER_SIZE];
        System.arraycopy(packet, 0, header, 0, header.length);

        if (!isHeaderValid(header)) {
            throw new IllegalArgumentException("Invalid header");
        }

        int payLoadLength = header[Config.HEADER_LENGTH_POS];
        byte[] payload = new byte[payLoadLength];
        System.arraycopy(packet, header.length, payload, 0, payLoadLength);

        System.out.println("Packet successfully disassembled.");

        return new String(payload, StandardCharsets.UTF_8);
    }

    private boolean isHeaderValid(byte[] header) {
        byte[] magic = Config.HEADER_MAGIC;
        for (int i = 0; i < magic.length; i++) {
            if (header[i] != magic[i]) {
                System.out.println("Invalid header magic");
                return false;
            }
        }
        System.out.println("Header magic is valid.");

        System.out.println("Header version: " + header[Config.HEADER_VERSION_POS]);

        return true;
    }

}
