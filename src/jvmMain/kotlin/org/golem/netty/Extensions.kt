package org.golem.netty

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import org.golem.netty.codec.encode
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.experimental.and
import kotlin.experimental.inv

fun ByteBuf.readAddress(): InetSocketAddress {
    val addressBytes: ByteArray
    val port: Int
    when(val type: Int = readByte().toInt()) {
        4 -> {
            addressBytes = readToByteArray(4).map { it.inv() and 0xFF.toByte() }.toByteArray()
            port = readUnsignedShort()
        }
        6 -> {
            readShortLE() // AF_INET6
            port = readUnsignedShort()
            readInt() // Flow Info
            addressBytes = readToByteArray(16)
            readInt() // Scope ID
        }
        else -> throw IllegalArgumentException("Unknown address type $type")
    }
    return InetSocketAddress(InetAddress.getByAddress(addressBytes), port)
}

fun ByteBuf.writeAddress(address: InetSocketAddress) {
    when (val inner: InetAddress = address.address) {
        is Inet4Address -> {
            this.writeByte(4) //IPv4
            val result = ByteArray(inner.address.size)
            for (i in 0 until inner.address.size) {
                result[i] = (inner.address[i] and 0xFF.toByte()).inv()
            }
            this.writeBytes(result)
            this.writeShort(address.port)
        }
        is Inet6Address -> {
            this.writeByte(6) // IPv6
            this.writeShortLE(10) // AF_INET6
            this.writeShort(address.port)
            this.writeInt(0) // Flow info
            this.writeBytes(inner.address)
            this.writeInt(inner.scopeId)
        }
    }
}

fun ByteBuf.readToByteArray(length: Int): ByteArray {
    val bytes = ByteArray(length)
    readBytes(bytes)
    return bytes
}

fun ByteBuf.split(maxSize: Int): MutableList<ByteBuf> {
    var current = ByteBufAllocator.DEFAULT.ioBuffer()
    val splitBuffers = mutableListOf<ByteBuf>()
    while(this.isReadable) {
        if(current.readableBytes() + this.readableBytes() > maxSize) {
            splitBuffers.add(current)
            current.slice()
            current = ByteBufAllocator.DEFAULT.ioBuffer()
        }
    }
    splitBuffers.add(current)
    return splitBuffers
}