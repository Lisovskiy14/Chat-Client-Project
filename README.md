# Chat-Client-Project

### The custom protocol general rules:
- Header (32 bytes):
  - Magic bytes (10 bytes) of constant data
		used for verifying packets of a custom
		chat.
  - Protocol version (1 byte). Used for supporting various versions of our custom protocol.
  - Length bytes (1 byte). Used for determining the length of payload.
  - Reserve bytes (20 bytes). Used like a reserve for future header protocol updates.
- Payload. Some data with length determined in the header.
- Transmissing ending:
  - end of transmitting data should be marked
	  with (byte representation): "EOT\r\n\r\n".
	  however that's also can be omitted if wanted,
	  the message will follow the "special" message
	  rule as for the headerless one.

This project is not a final implementation. It can be updated at any time. 🧑‍🦽‍➡️
