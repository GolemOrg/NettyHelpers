package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class DoubleLE(private val value: Double): Encodable, Decodable {
    override fun encode(buffer: ByteBuf) { buffer.writeDoubleLE(value) }
    override fun decode(buffer: ByteBuf): DoubleLE = DoubleLE(buffer.readDoubleLE())
    fun toDouble(): Double = value
    override fun toString(): String = value.toString()
}

fun Double.toDoubleLE() = DoubleLE(this)
fun Float.toDoubleLE() = DoubleLE(this.toDouble())