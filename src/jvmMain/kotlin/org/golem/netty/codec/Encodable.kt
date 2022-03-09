package org.golem.netty.codec

import io.netty.buffer.ByteBuf

interface Encodable {
    fun encode(buffer: ByteBuf)
}