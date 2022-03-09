package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class UMediumLE(private val value: UInt): Encodable, Decodable {
    override fun encode(buffer: ByteBuf) { buffer.writeMediumLE(value.toInt()) }
    override fun decode(buffer: ByteBuf): UMediumLE = UMediumLE(buffer.readUnsignedMediumLE().toUInt())
    fun toInt(): Int = value.toInt()
    fun toUInt(): UInt = value
    override fun toString(): String = value.toString()
}

fun UInt.toUMediumLE() = UMediumLE(this)
fun Int.toUMediumLE() = UMediumLE(this.toUInt())