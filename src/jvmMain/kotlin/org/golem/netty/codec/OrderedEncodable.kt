package org.golem.netty.codec

import io.netty.buffer.ByteBuf

interface OrderedEncodable: Encodable {
    fun encodeOrder(): Array<Any>

    override fun encode(buffer: ByteBuf) {

    }
}