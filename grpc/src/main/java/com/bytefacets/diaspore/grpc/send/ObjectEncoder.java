package com.bytefacets.diaspore.grpc.send;

/**
 * @see ObjectEncoderRegistry#register(Class, ObjectEncoder)
 */
public interface ObjectEncoder {
    /**
     * Extension point for implementation to encode the `value` into the ByteBuffer returned by the
     * BufferSupplier.
     *
     * @param bufferSupplier given a type and length, it will return you an appropriately sized and
     *     positioned ByteBuffer
     * @param value the value to encode
     */
    void encode(BufferSupplier bufferSupplier, Object value);
}
