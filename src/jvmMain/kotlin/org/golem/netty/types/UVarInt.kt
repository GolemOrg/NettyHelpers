package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.*
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class UVarInt(val value: UInt): Encodable, Decodable {
    override fun decode(buffer: ByteBuf): UVarInt {
        var output = 0u
        for(i in VARINT_ITERATOR) {
            val current = buffer.readByte().toUInt()
            output = output or (current and VARTYPE_MAX_BYTE_VALUE.toUInt() shl i)
            if (current and VARTYPE_BIT_FLAG.toUInt() == 0u) {
                return output.toUVarInt()
            }
        }
        throw IllegalArgumentException("VarInt is too large")
    }

    override fun encode(buffer: ByteBuf) {
        var input = value
        var hasMore = input and VARTYPE_MAX_BYTE_VALUE.toUInt().inv() != 0u
        var writtenBytes = 0
        do {
            if(writtenBytes++ > VARINT_MAX_ITERATIONS) {
                throw IllegalArgumentException("UVarInt is too large")
            }
            val flag = if (hasMore) VARTYPE_BIT_FLAG.toUInt() else 0u
            buffer.writeByte((input and VARTYPE_MAX_BYTE_VALUE.toUInt() or flag).toInt())
            input = input shr VARTYPE_STEP_SIZE
            hasMore = input and VARTYPE_MAX_BYTE_VALUE.toUInt().inv() != 0u
        } while(hasMore)
    }

    fun toInt() = value.toInt()
    fun toUInt(): UInt = value
    override fun toString(): String = value.toString()
}

fun UInt.toUVarInt() = UVarInt(this)
fun Int.toUVarInt() = UVarInt(this.toUInt())