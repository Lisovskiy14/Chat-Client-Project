package com.chatclient.config;

import java.util.ArrayList;
import java.util.List;

public class ByteParser {

    public static byte[] parse(String input) {
        List<Byte> bytes = new ArrayList<>();

        for (int i = 0; i < input.length();) {
            if (input.charAt(i) == '\\' && i + 4 < input.length()) {
                String hex = input.substring(i + 3, i + 5);
                byte b = (byte) Integer.parseInt(hex, 16);
                bytes.add(b);
                i += 5;
            } else {
                byte b = (byte) input.charAt(i);
                bytes.add(b);
                i++;
            }
        }

        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }

        return result;
    }
}
