package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class LongLE(private val value: Long): Encodable, Decodable {
    override fun encode(buffer: ByteBuf) { buffer.writeLongLE(value) }
    override fun decode(buffer: ByteBuf): LongLE = LongLE(buffer.readLongLE())
    fun toLong(): Long = value
    fun toULong(): ULong = value.toULong()
    override fun toString(): String = value.toString()
}

fun Long.toLongLE() = LongLE(this)
fun ULong.toLongLE() = LongLE(this.toLong())