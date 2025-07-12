package com.bytefacets.diaspore.grpc.receive;

import java.nio.ByteBuffer;

/**
 * @see com.bytefacets.diaspore.grpc.send.ObjectEncoder
 * @see ObjectDecoderRegistry#register(byte, ObjectDecoder)
 */
public interface ObjectDecoder {
    /**
     * Object decoding extension point
     *
     * @param buffer the buffer with the encoded value positioned for reading
     * @return the decoded object
     */
    Object decode(ByteBuffer buffer);
}
