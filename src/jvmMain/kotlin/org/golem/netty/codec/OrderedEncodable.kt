package org.golem.netty.codec

import io.netty.buffer.ByteBuf
import org.golem.netty.readAddress
import org.golem.netty.readToByteArray
import org.golem.netty.writeAddress
import java.net.InetSocketAddress

interface OrderedEncodable: Encodable {
    fun encodeOrder(): Array<Any>

    override fun encode(buffer: ByteBuf) {
        for(value in encodeOrder()) {
            value.encode(buffer)
        }
    }
}

fun Any?.decode(buffer: ByteBuf): Any? {
    return when(this) {
        is Byte -> buffer.readByte()
        is Short -> buffer.readShort()
        is Int -> buffer.readInt()
        is Long -> buffer.readLong()
        is Float -> buffer.readFloat()
        is Double -> buffer.readDouble()
        is Char -> buffer.readChar()
        is Boolean -> buffer.readBoolean()
        is ByteArray -> buffer.readToByteArray(this.size)
        is ByteBuf -> buffer.readBytes(this)
        is InetSocketAddress -> buffer.readAddress()
        is Decodable -> this.decode(buffer)
        else -> null
    }
}

fun Any?.encode(buffer: ByteBuf) {
    when (this) {
        is Byte -> buffer.writeByte(this.toInt())
        is Boolean -> buffer.writeBoolean(this)
        is Short -> buffer.writeShort(this.toInt())
        is Int -> buffer.writeInt(this)
        is Long -> buffer.writeLong(this)
        is Float -> buffer.writeFloat(this)
        is Double -> buffer.writeDouble(this)
        is ByteArray -> buffer.writeBytes(this)
        is ByteBuf -> buffer.writeBytes(this)
        is InetSocketAddress -> buffer.writeAddress(this)
        is Encodable -> this.encode(buffer)
        is Array<*> -> this.forEach { it.encode(buffer) }
        is List<*> -> this.forEach { it.encode(buffer) }
        else -> {
            val type = this?.javaClass!!.simpleName ?: "null"
            throw IllegalArgumentException("Encountered unknown type: $type when encoding value to buffer")
        }
    }
}