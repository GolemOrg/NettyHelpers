package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.*
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable


@JvmInline
value class UVarLong(val value: ULong): Encodable, Decodable {
    override fun decode(buffer: ByteBuf): UVarLong {
        var output = 0uL
        for(i in VARLONG_ITERATOR) {
            val current = buffer.readByte().toULong()
            output = output or (current and VARTYPE_MAX_BYTE_VALUE.toULong() shl i)
            if (current and VARTYPE_BIT_FLAG.toULong() == 0uL) {
                return UVarLong(output)
            }
        }
        throw IllegalArgumentException("VarInt is too large")
    }

    override fun encode(buffer: ByteBuf) {
        var input = value
        var hasMore = input and VARTYPE_MAX_BYTE_VALUE.toULong().inv() != 0uL
        var writtenBytes = 0
        do {
            if(writtenBytes++ > VARLONG_MAX_ITERATIONS) {
                throw IllegalArgumentException("VarLong is too large")
            }
            val flag = if (hasMore) VARTYPE_BIT_FLAG.toULong() else 0uL
            buffer.writeByte((input and VARTYPE_MAX_BYTE_VALUE.toULong() or flag).toInt())
            input = input shr VARTYPE_STEP_SIZE
            hasMore = input and VARTYPE_MAX_BYTE_VALUE.toULong().inv() != 0uL
        } while(hasMore)
    }

    fun toLong() = value.toLong()
    fun toULong(): ULong = value
}

fun ULong.toUVarLong() = UVarLong(this)
fun Long.toUVarLong() = UVarLong(this.toULong())