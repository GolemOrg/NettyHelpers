package org.golem.netty.types

import io.netty.buffer.ByteBuf
import org.golem.netty.*
import org.golem.netty.codec.Decodable
import org.golem.netty.codec.Encodable

@JvmInline
value class VarInt(private val value: Int): Encodable, Decodable {
    override fun decode(buffer: ByteBuf): VarInt {
        var output = 0
        for(i in VARINT_ITERATOR) {
            val current = buffer.readByte().toInt()
            output = output or (current and VARTYPE_MAX_BYTE_VALUE shl i)
            if (current and VARTYPE_BIT_FLAG == 0) {
                return output.toVarInt()
            }
        }
        throw IllegalArgumentException("VarInt is too large")
    }

    override fun encode(buffer: ByteBuf) {
        var input = value
        var hasMore = input and VARTYPE_MAX_BYTE_VALUE.inv() != 0
        var writtenBytes = 0
        do {
            if(writtenBytes++ > VARINT_MAX_ITERATIONS) {
                throw IllegalArgumentException("VarInt is too large")
            }
            val flag = if (hasMore) VARTYPE_BIT_FLAG else 0
            buffer.writeByte(input and VARTYPE_MAX_BYTE_VALUE or flag)

            input = input shr VARTYPE_STEP_SIZE
            hasMore = input and VARTYPE_MAX_BYTE_VALUE.inv() != 0
        } while(hasMore)
    }

    fun toInt() = value
    fun toUInt(): UInt = value.toUInt()
    override fun toString(): String = value.toString()
}
fun Int.toVarInt() = VarInt(this)
fun UInt.toVarInt() = VarInt(this.toInt())