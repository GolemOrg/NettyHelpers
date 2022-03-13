# Netty Helpers

This is a small Kotlin library used to enhance Netty's functionality by adding types, interfaces, and extensions.
NOTE: The encoding / decoding here is used specifically for the [`RakNet`](https://github.com/GolemOrg/RakNet) library.

## Little Endian
For types, it adds support for these little endian types:
- Int
- Long
- Double
- Float
- UInt
- ULong
- UMedium

All little endian versions of these types are suffixed with `LE` (e.g. `IntLE`).

Further support for more types will occur in the future.

## Var Encoding
For types, it adds support for these var encoding types:
- VarInt
- VarLong
- UVarInt
- UVarLong

More information about this var encoding can be found [here](https://developers.google.com/protocol-buffers/docs/encoding).


## Interfaces
For interfaces, it adds support three interfaces.

### Decodable
This interface is used to decode a message from a ByteBuf.

### Encodable
This interface is used to encode a message to a ByteBuf.

### OrderedEncodable
This interface requires an `encodeOrder()` method to be implemented with an array of any values needed to encode the message.
This interface implements the `Encodable` interface and overrides it with a default implementation.

## Extensions
Lastly, there are extensions. For the `ByteBuf` type, it adds support for these extensions:
- `ByteBuf.readAddress(): InetSocketAddress`
- `ByteBuf.writeAddress(address: InetSocketAddress): ByteBuf`
- `ByteBuf.readVarInt(): VarInt`
- `ByteBuf.writeVarInt(value: Int): ByteBuf`
- `ByteBuf.readVarLong(): VarLong`
- `ByteBuf.writeVarLong(value: Long): ByteBuf`
- `ByteBuf.readUVarInt(): UVarInt`
- `ByteBuf.writeUVarInt(value: UInt): ByteBuf`
- `ByteBuf.readUVarLong(): UVarLong`
- `ByteBuf.writeUVarLong(value: ULong): ByteBuf`
- `ByteBuf.readToByteArray(length: Int): ByteArray` - Given a length, it reads the next `length` bytes from the buffer and returns them as a byte array.
- `ByteBuf.split(maxSize: Int): MutableList<ByteBuf>` - Given a max size, it splits the buffer into multiple buffers each equaling or less than the max size.

Internally, there are extensions for all types (`Any?`). These are used in the `OrderedEncodable` interface as 
a way to automatically encode each field using `field.encode(buffer)`
