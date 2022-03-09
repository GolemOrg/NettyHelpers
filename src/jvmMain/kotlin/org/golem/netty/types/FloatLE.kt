package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class FloatLE(private val value: Float): Encodable, Decodable {
    override fun encode(buffer: ByteBuf) { buffer.writeFloatLE(value) }
    override fun decode(buffer: ByteBuf): FloatLE = FloatLE(buffer.readFloatLE())
    fun toFloat(): Float = value
    override fun toString(): String = value.toString()
}

fun Float.toFloatLE() = FloatLE(this)
fun Double.toFloatLE() = FloatLE(this.toFloat())