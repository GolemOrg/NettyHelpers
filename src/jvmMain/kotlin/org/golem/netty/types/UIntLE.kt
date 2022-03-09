package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class UIntLE(private val value: UInt): Encodable, Decodable {
    override fun encode(buffer: ByteBuf) { buffer.writeIntLE(value.toInt()) }
    override fun decode(buffer: ByteBuf): IntLE = IntLE(buffer.readIntLE())
    fun toInt(): Int = value.toInt()
    fun toUInt(): UInt = value
    override fun toString(): String = value.toString()
}

fun UInt.toUIntLE() = UIntLE(this)
fun Int.toUIntLE() = UIntLE(this.toUInt())

