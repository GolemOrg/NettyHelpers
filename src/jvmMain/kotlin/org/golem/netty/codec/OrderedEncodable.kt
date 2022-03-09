package org.golem.netty.codec

import io.netty.buffer.ByteBuf
import org.golem.netty.readToByteArray
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.experimental.and
import kotlin.experimental.inv

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
        is Encodable -> this.encode(buffer)
        is InetSocketAddress -> {
            when (val inner: InetAddress = this.address) {
                is Inet4Address -> {
                    buffer.writeByte(4) //IPv4
                    val result = ByteArray(inner.address.size)
                    for (i in 0 until inner.address.size) {
                        result[i] = (inner.address[i] and 0xFF.toByte()).inv()
                    }
                    buffer.writeBytes(result)
                    buffer.writeShort(this.port)
                }
                is Inet6Address -> {
                    buffer.writeByte(6) // IPv6
                    buffer.writeShortLE(10) // AF_INET6
                    buffer.writeShort(this.port)
                    buffer.writeInt(0) // Flow info
                    buffer.writeBytes(inner.address)
                    buffer.writeInt(inner.scopeId)
                }
            }
        }
        is Array<*> -> this.forEach { it.encode(buffer) }
        is List<*> -> this.forEach { it.encode(buffer) }
        else -> {
            val type = this?.javaClass!!.simpleName ?: "null"
            throw IllegalArgumentException("Encountered unknown type: $type when encoding value to buffer")
        }
    }
}