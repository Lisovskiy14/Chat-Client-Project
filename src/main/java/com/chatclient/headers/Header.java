package com.chatclient.headers;

import com.chatclient.config.Config;

public interface Header {
    int LENGTH = Config.HEADER_SIZE;

    byte[] collect(int payloadLength);
}
