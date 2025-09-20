package com.chatclient.headers.impls;

import com.chatclient.config.Config;
import com.chatclient.headers.Header;

public class ChatHeaderV1 implements Header {

    @Override
    public byte[] collect(int payloadLength) {
        byte[] header = new byte[Header.LENGTH];

        // Adding magic
        byte[] magic = Config.HEADER_MAGIC;
        System.arraycopy(magic, 0, header, 0, 10);

        // Adding protocol version
        header[Config.HEADER_VERSION_POS] = 1;

        // Adding length of payload
        header[Config.HEADER_LENGTH_POS] = (byte) payloadLength;

        // Reserving bytes by 0
        for (int i = 12; i < header.length; i++) {
            header[i] = 0;
        }

        return header;
    }
}
