package com.chatclient.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String get(String key) {
        return properties.getProperty(key);
    }


    // All config values
    public static int HEADER_SIZE = Integer.parseInt(get("protocol.header.size"));
    public static final byte[] HEADER_MAGIC;
    public static final byte HEADER_VERSION_POS = Byte.parseByte(get("protocol.header.pos.version"));
    public static final byte HEADER_LENGTH_POS = Byte.parseByte(get("protocol.header.pos.length"));
    public static final byte[] EOT = get("protocol.eot").getBytes(StandardCharsets.US_ASCII);

    static {
        // Parsing HEADER_MAGIC
        String strMagic = get("protocol.header.magic");
        HEADER_MAGIC = ByteParser.parse(strMagic);
    }
}
