package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class ULongLE(private val value: ULong): Encodable, Decodable {
    override fun encode(buffer: ByteBuf) { buffer.writeLongLE(value.toLong()) }
    override fun decode(buffer: ByteBuf): LongLE = LongLE(buffer.readLongLE())
    fun toLong(): Long = value.toLong()
    fun toULong(): ULong = value
    override fun toString(): String = value.toString()
}

fun ULong.toULongLE() = ULongLE(this)
fun Long.toULongLE() = ULongLE(this.toULong())