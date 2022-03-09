package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class IntLE(private val value: Int): Encodable, Decodable {
    override fun encode(buffer: ByteBuf) { buffer.writeIntLE(value) }
    override fun decode(buffer: ByteBuf): IntLE = IntLE(buffer.readIntLE())
    fun toInt(): Int = value
    fun toUInt(): UInt = value.toUInt()
    override fun toString(): String = value.toString()
}

fun Int.toIntLE() = IntLE(this)
fun UInt.toIntLE() = IntLE(this.toInt())