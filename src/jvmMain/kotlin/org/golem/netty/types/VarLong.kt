package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.*
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class VarLong(val value: Long): Encodable, Decodable {
    override fun decode(buffer: ByteBuf): VarLong {
        var output = 0L
        for(i in VARLONG_ITERATOR) {
            val current = buffer.readByte().toLong()
            output = output or (current and VARTYPE_MAX_BYTE_VALUE.toLong() shl i)
            if (current and VARTYPE_BIT_FLAG.toLong() == 0L) {
                return VarLong(output)
            }
        }
        throw IllegalArgumentException("VarInt is too large")
    }

    override fun encode(buffer: ByteBuf) {
        var input = value
        var hasMore = input and VARTYPE_MAX_BYTE_VALUE.toLong().inv() != 0L
        var writtenBytes = 0
        do {
            if(writtenBytes++ > VARLONG_MAX_ITERATIONS) {
                throw IllegalArgumentException("VarLong is too large")
            }
            val flag = if (hasMore) VARTYPE_BIT_FLAG.toLong() else 0L
            buffer.writeByte((input and VARTYPE_MAX_BYTE_VALUE.toLong() or flag).toInt())
            input = input shr VARTYPE_STEP_SIZE
            hasMore = input and VARTYPE_MAX_BYTE_VALUE.toLong().inv() != 0L
        } while(hasMore)
    }

    fun toLong() = value
    fun toULong(): ULong = value.toULong()
}

fun Long.toVarLong() = VarLong(this)
fun ULong.toVarLong() = VarLong(this.toLong())